# Command
ssh -A -F <sshconfig file> <symbolic name>

# sshconfig file
# Commets of entry one
Host github-codehere # symbolic name
    HostName github.com # To be replaced on user@[SymbolicName]...
    User git # User (useful for git)
    IdentityFile ~/.ssh/github_rsa # Take this private key

# Comments of entry two
Host idontknowrick
    HostName domain.com
    IdentityFile ~/.ssh/id_rsa
