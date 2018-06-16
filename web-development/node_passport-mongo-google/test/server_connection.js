'use strict'

const request = require('request');
const assert = require('assert');
const should = require('should');

describe('connection (only check code)', function() {
    it('should connect server', function(done) {
        request
            .get('http://localhost:3000')
            .on('response', function(res) {
                res.statusCode.should.equal(200);
                console.log('body');
                done();
            })
    });
});

describe('passport authentication', function() {
    it('get authentication', function(done) {
        request
            .get('http://localhost:3000/auth/google')
            .on('response', function(res) {
                console.log('headers', res.headers['set-cookie']);
                console.log('body', res);
                console.log('user', res.user);
                var setCookie = res.headers['set-cookie'];
                should.exist(setCookie);
                done();
            })
    });
});
