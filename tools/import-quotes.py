import json
from datetime import datetime

pretty_quotes = []

with open('quotes.json') as quotes_file:
    quotes_json = json.load(quotes_file)

for authorid, quotes in quotes_json.items():
    for messageid, quote in quotes_json[authorid].items():
        timestamp = int(datetime.strptime(quote["time"], "%Y-%m-%d %H:%M").timestamp())
        author = quote["author"]
        quoter = quote["adder"]

        if "image" in quote:
            content = quote["image"] + " " + quote["content"]
        else:
            content = quote["content"]

        pretty_quote = {
            "id": messageid,
            "author": authorid,
            "authorName": author,
            "quoter": -1,
            "quoterName": quoter,
            "channel": -1,
            "timestamp": timestamp,
            "content": content
        }

        pretty_quotes.append(pretty_quote)

with open('pretty_quotes.json', 'w') as pretty_quotes_file:
    json.dump(pretty_quotes, pretty_quotes_file)


