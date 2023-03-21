const express = require('express');
const geoHash = require('ngeohash');
const axios = require('axios');
const cors = require('cors');

const app = express();

const PORT = process.env.PORT || 5000;

const apikey = 'pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb';

const categoryMap = {'Music': 'KZFzniwnSyZfZ7v7nJ', 'Sports': 'KZFzniwnSyZfZ7v7nE', 'Arts': 'KZFzniwnSyZfZ7v7na',
    'Film': 'KZFzniwnSyZfZ7v7nn', 'Miscellaneous': 'KZFzniwnSyZfZ7v7n1', 'Default': ''};

app.use(cors({ origin: 'http://localhost:4200' }));

app.get('/', (req, res) => {
    const dates = [
        "2020-04-10 15:00:00",
        "2020-04-11",
        "2020-04-09 12:30:00",
        "2020-04-10"
    ];
    const sortedDates = dates.sort((a, b) => new Date(a) - new Date(b));
    res.send(sortedDates);
});

function preprocessSearchData(data) {
    let returnData = [];
    for (let i = 0; i < data.length; i++) {
        if (returnData.length === 20) break;
        let event = {};
        if (data[i].hasOwnProperty('id')) {
            event['id'] = data[i]['id'];
        }
        else continue;
        if (data[i].hasOwnProperty('name')) {
            event['eventName'] = data[i]['name'];
        }
        else continue;
        if (data[i].hasOwnProperty('dates')) {
            event['dates'] = data[i]['dates']['start']['localDate'] + (data[i]['dates']['start'].hasOwnProperty('localTime') ? '\n' + data[i]['dates']['start']['localTime'] : '');
        }
        else continue
        if (data[i].hasOwnProperty('images')) {
            event['images'] = data[i]['images'][0]['url'];
        }
        else continue
        if (data[i].hasOwnProperty('classifications') && data[i]['classifications'].length > 0 && data[i]['classifications'][0].hasOwnProperty('segment')
            && data[i]['classifications'][0]['segment'].hasOwnProperty('name') && data[i]['classifications'][0]['segment']['name'] !== 'Undefined') {
            event['genre'] = data[i]['classifications'][0]['segment']['name'];
        }
        else continue
        if (data[i].hasOwnProperty('_embedded') && data[i]['_embedded'].hasOwnProperty('venues') && data[i]['_embedded']['venues'].length > 0) {
            event['venue'] = data[i]['_embedded']['venues'][0]['name'];
        }
        else continue
        returnData.push(event);
    }
    return returnData;
}
app.get('/search/:data', async (req, res) => {
    let data = JSON.parse(req.params['data']);
    console.log(data);
    let loc = data['location'].split(',');
    let lat = loc[0];
    let lng = loc[1];
    let geoPoint = geoHash.encode(lat, lng, 7);
    let radius = data['distance'];
    let keyword = data['keyword'].split(' ').join('+');
    let unit = 'miles';
    let segmentId = categoryMap[data['category']];
    let url = `https://app.ticketmaster.com/discovery/v2/events.json?apikey=${apikey}&keyword=${keyword}&radius=${radius}&unit=${unit}&geoPoint=${geoPoint}&segmentId=${segmentId}`;
    try {
        const response = await axios.get(url);
        // console.log(response.data)
        if ('_embedded' in response.data && 'events' in response.data['_embedded']) {
            let events = response.data['_embedded']['events'];
            let returnData = preprocessSearchData(events);
            res.status(200).json(returnData);
        }
        else {
            res.status(200).json(null);
        }
    } catch (error) {
        console.log(error);
        res.status(500).json(null);
    }
})

app.get('/autocomplete/:keyword', async (req, res) => {
    let keyword = req.params['keyword'];
    let url = `https://app.ticketmaster.com/discovery/v2/suggest?apikey=${apikey}&keyword=${keyword}`;
    try {
        const response = await axios.get(url);
        if ('_embedded' in response.data && 'attractions' in response.data['_embedded']) {
            let attractions = response.data['_embedded']['attractions'].slice(0, 5);
            let suggestions = [];
            attractions.forEach(attraction => {
                suggestions.push(attraction['name']);
            })
            // console.log(suggestions)
            res.status(200).json(suggestions);
        }
        else {
            res.status(200).json(null);
        }
    } catch (error) {
        console.log(error);
        res.status(500).json(null);
    }
})


app.listen(PORT, () => console.log(`Server started on port ${PORT}`));
