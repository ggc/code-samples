# A commit message should add/answer this:

## Why is this change necessary?

This question tells reviewers of your pull request what to expect in the commit, allowing them to more easily identify and point out unrelated changes.

## How does it address the issue?

Describe, at a high level, what was done to affect change. Introduce a red/black tree to increase search speed or Remove <troublesome gem X>, which was causing <specific description of issue introduced by gem> are good examples.

If your change is obvious, you may be able to omit addressing this question.

## What side effects does this change have?

This is the most important question to answer, as it can point out problems where you are making too many changes in one commit or branch. One or two bullet points for related changes may be okay, but five or six are likely indicators of a commit that is doing too many things.

Your team should have guidelines and rules-of-thumb for how much can be done in a single commit/branch.

## Including a link to the issue/story/card in the commit message a standard for your project. 

Full urls are more useful than issue numbers, as they are more permanent and avoid confusion over which issue tracker it references.


# Sample

	Redirect user to the requested page after login
	
	https://trello.com/path/to/relevant/card
	
	Users were being redirected to the home page after login, which is less
	useful than redirecting to the page they had originally requested before
	being redirected to the login form.
	
	* Store requested path in a session variable
	* Redirect to the stored location after successfully logging in the user
	

# Extra: Git repository maintenance
`git fsck` to validate repo is healthy
`git gc` to compress repo
`git stash list` to see stash status
