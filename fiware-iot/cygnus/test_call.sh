curl -X POST "http://172.17.0.5:8081/v1/subscriptions?ngsi_version=2" -d '{"subscription":{"description": "title_of_subscription","subject": {"entities": [{"idPattern": ".*","type": "Room"}],"condition": {"attrs": ["attr1"],"expression": {"q": "attr1>40"}}},"notification": {"http": {"url": "http://localhost:1234"},"attrs": ["attr1","attr2"]},"expires": "2016-05-05T14:00:00.00Z","throttling": 5}, "endpoint":{"host":"172.17.0.1", "port":"1026", "ssl":"false", "xauthtoken":"your_auth_token"}}'

