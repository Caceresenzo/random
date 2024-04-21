import requests
import json
import sseclient
import types


def handle_sse(response: requests.Response):
    client = sseclient.SSEClient(response)
    for event in client.events():
        event.data = json.loads(event.data)
        yield event

def call(sse: bool):
    response = requests.get("http://localhost:8080/hello", params={
        "sse": sse
    }, stream=True)

    if response.headers.get("content-type") == "text/event-stream":
        return handle_sse(response)

    return response.json()


result = call(True)
if isinstance(result, types.GeneratorType):
    for event in result:
        # print(vars(event))
        print(event.event, event.data)
else:
    print(result)
