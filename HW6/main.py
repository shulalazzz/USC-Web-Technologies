import json
from flask import Flask, jsonify, send_from_directory
from geolib import geohash
import requests

app = Flask(__name__, static_url_path='')

# Ipinfo API
# fcee7187512c64
# Google Maps API
# AIzaSyAhrUOOniYwPz_aLnuKi2M6v3DfG50oH5o
# tickermasterapi
# Consumer Key	pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb
# Consumer Secret	VAQWgL5UV5oiIaoV

categoryMap = {'Music': 'KZFzniwnSyZfZ7v7nJ', 'Sports': 'KZFzniwnSyZfZ7v7nE', 'Arts': 'KZFzniwnSyZfZ7v7na',
               'Film': 'KZFzniwnSyZfZ7v7nn', 'Miscellaneous': 'KZFzniwnSyZfZ7v7n1', 'Default': ''}


@app.route('/')
def index():
    return send_from_directory('static', 'index.html')


@app.route('/search/<data>', methods=['GET'])
def search(data):
    jsonData = json.loads(data)
    print(jsonData)
    apikey = 'pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb'
    lat, lng = jsonData['location'].split(',')
    geoPoint = geohash.encode(float(lat), float(lng), precision=7)
    radius = (jsonData['distance'])
    segmentId = categoryMap[jsonData['category']]
    unit = 'miles'
    keyword = '+'.join(jsonData['keyword'].split(' '))
    response = requests.get('https://app.ticketmaster.com/discovery/v2/events.json',
                            params={'apikey': apikey, 'geoPoint': geoPoint, 'radius': radius, 'unit': unit,
                                    'keyword': keyword, 'segmentId': segmentId})
    if response.status_code == 200:
        if '_embedded' in response.json() and 'events' in response.json()['_embedded']:
            events = response.json()['_embedded']['events'][:20]
            # json_string = json.dumps(events)
            # print(json_string)
            return jsonify(events)
    return jsonify(None)


@app.route('/event/<event_id>', methods=['GET'])
def get_event_detail(event_id):
    apikey = 'pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb'
    response = requests.get('https://app.ticketmaster.com/discovery/v2/events/' + event_id,
                            params={'apikey': apikey})
    if response.status_code == 200:
        if 'name' in response.json():
            event_detail = response.json()
            json_string = json.dumps(event_detail)
            # print(json_string)
            return jsonify(event_detail)
    return jsonify(None)


@app.route('/venue/<venue_name>', methods=['GET'])
def get_venue_detail(venue_name):
    print(venue_name)
    apikey = 'pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb'
    response = requests.get('https://app.ticketmaster.com/discovery/v2/venues',
                            params={'apikey': apikey, 'keyword': venue_name})
    if response.status_code == 200:
        if '_embedded' in response.json() and 'venues' in response.json()['_embedded']:
            venue_detail = response.json()['_embedded']['venues'][0]
            json_string = json.dumps(venue_detail)
            # print(json_string)
            return jsonify(venue_detail)
    return jsonify(None)


if __name__ == '__main__':
    app.run(debug=True)
