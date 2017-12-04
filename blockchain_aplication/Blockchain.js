var sha256 = require('js-sha256').sha256,
    http = require('http'),
    url = require('url'),
    restify = require('restify');

function Blockchain() {
    this.chain = [];
    this.currentTransaction = [];
    this.nodes = {};

    this.registerNode = function registerNode(address) {
        var parsedUrl = url.parse(address);
        this.nodes[""+parsedUrl.host] = true;
    }

    this.validChain = function validChain(chain) {
        var lastBlock = chain[0],
            current = 1;

        while(current < chain.length) {
            var block = chain[current];
            console.log('>>> lastBlock', lastBlock);
            console.log('>>> block', block);
            console.log('-----');

            if( block.previousHash != this.hash(lastBlock)) {
                return false;
            }

            if(!this.validProof(lastBlock.proof, block.proof)) {
                return false;
            }

            lastBlock = block;
            current += 1;
        }

        return true;
    };
    
    this.resolveConflicts = function resolveConflicts() {
        var that = this;
        return new Promise(function(resolve, reject) {
            var neighbours = that.nodes,
                newChain = null,
                maxLength = that.chain.length,
                requests = [];

            for (node in neighbours) {
                var req = {
                    "host": 'http://' + node.split(':')[0],
                    "path": "/chain",
                    "port": node.split(':')[1],
                    "method": "GET",
                    "headers": {
                        "Accept": "application/json"
                    }
                };
                requests.push(new Promise(function(resolve, reject) {
                    var req = http.request('http://' + node + '/chain', function(res) {
                        res.on('data', function(chunk) {
                            var chunked = JSON.parse(chunk),
                                length = chunked.length,
                                chain = chunked.chain;
console.log('>>> chunk values', length, chain, 'full chunk:', chunk);
                            if (length > maxLength && that.validChain(chain)) {
                                maxLength = length;
                                newChain = chain;
                                console.log('>>> New chain set');
                            } 
                        });
                        res.on('end', () => {
                            console.log('GET end. Resolving with new chain:', newChain);
                            resolve(newChain);
                        });
                    });
                    req.on('error', function() {
                        resolve(null);
                    })
                    req.end();
                }))
            };

            Promise.all(requests).then(function(values) {
                for (newChain in values) {
                    if(newChain != null) {
                        this.chain = newChain;
                        return resolve(true);
                    }
                }
                return resolve(false);
            });
        })
    };

    this.lastBlock = function lastBlock() {
        return this.chain[this.chain.length - 1];
    }

    this.newBlock = function newBlock(proof, previousHash) {
        var block = {
            "index": this.chain.length,
            "timestamp": new Date(),
            "transactions": this.currentTransaction,
            "proof": proof,
            "previousHash": previousHash || hash(this.chain[this.chain.length - 1])
        }

        this.currentTransaction = [];
        this.chain.push(block);

        return block;
    };

    this.newTransaction = function newTransaction(sender, recipient, amount) {
        this.currentTransaction.push({
            "sender": sender,
            "recipient": recipient,
            "amount": amount
        });

        return this.lastBlock() + 1;
    };

    this.proofOfWork = function proofOfWork(lastProof) {
        var proof = 0;
        while(this.validProof(lastProof, proof) == false) {
            proof += 1;
        }
        return proof;
    }

    this.validProof = function validProof(lastProof, proof) {
        // Keeping it simple...
        var guess = sha256(lastProof + '' + proof);
        // if (guess.substr(0,4) == '0000') {
        //     console.log('>>> proof', guess);
        // }
        return guess.substr(0,4) == '0000';
    }

    this.hash = function hash(block) {
        var blockDigest = JSON.stringify(block);
        return sha256(blockDigest);
    }

    this.newBlock(1, 100);
}




var Blockchain = new Blockchain();
Blockchain.registerNode('http://localhost:8000/');
// Blockchain.newBlock(1, 100);
// Blockchain.newTransaction('me', 'you', 1);
// Blockchain.proofOfWork('qwerty');
// console.log('>>> Length-', Blockchain.lastBlock());

var server = restify.createServer();
server.use(restify.plugins.jsonBodyParser({}));

server.get('/chain', function(req, res, next) {
    res.send({
        "chain": Blockchain.chain,
        "length": Blockchain.chain.length
    });
});

server.get('/mine', function(req, res, next) {
    var lastBlock = Blockchain.lastBlock(),
        lastProof = lastBlock.proof,
        proof = Blockchain.proofOfWork(lastProof);

    Blockchain.newTransaction('Me!', 'TheBlockChain', 1);

    var previousHash = Blockchain.hash(lastBlock),
        block = Blockchain.newBlock(proof, previousHash),
        response = {
            "message": "New Block Forged",
            "index": block["index"],
            "transactions": block["transactions"],
            "proof": block["proof"],
            "previousHash": block["previousHash"],
        };

    res.send(response);
});

server.post('/transaction/new', function(req, res, next) {
    var newTrans = req.body;
    console.log('>>> transaction received', newTrans);
    
    if( newTrans.sender == null || newTrans.recipient == null || newTrans.amount == null) {
        res.send(400, 'Missing values');
    }

    var index = Blockchain.newTransaction(newTrans.sender, newTrans.recipient, newTrans.amount);

    var response = {
        "message": "Transaction will be added to Block " + index
    };
    res.send(201, response);
});

server.post('/nodes/register', function(req, res, next) {
    var newNodes = req.body.nodes;

    if(newNodes == null) {
        res.send(400, 'Provide a list of nodes');
    }

    newNodes.forEach(function(node) {
        Blockchain.registerNode(node);
    });

    var response = {
        "message": "New nodes have been adde",
        "totalNodes": Blockchain.nodes
    }

    res.send(201, response);
});

server.get('/nodes/resolve', function(req, res, next) {
    var response;
    Blockchain.resolveConflicts().then( replaced => {
        console.log('>>> replaced?', replaced);
        if(replaced) {
            response = {
                "message": "Our chain was replaced",
                "newChain": Blockchain.chain
            }
        } else {
            response = {
                "message": "Our chain is authoritative",
                "chain": Blockchain.chain
            }
        }
        res.send(200, response);
    })
});

server.listen(process.env.PORT || 8000, function() {
    console.log('%s listening at %s', server.name, server.url);
});