const express = require('express');
const geoHash = require('ngeohash');
const axios = require('axios');
const cors = require('cors');
const SpotifyWebApi = require('spotify-web-api-node');

const app = express();

const PORT = process.env.PORT || 5000;

const apikey = 'pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb';

const categoryMap = {'Music': 'KZFzniwnSyZfZ7v7nJ', 'Sports': 'KZFzniwnSyZfZ7v7nE', 'Arts': 'KZFzniwnSyZfZ7v7na',
    'Film': 'KZFzniwnSyZfZ7v7nn', 'Miscellaneous': 'KZFzniwnSyZfZ7v7n1', 'Default': ''};

const spotifyApi = new SpotifyWebApi({
    clientId: '18b00b32220347659ee24add2e2029aa',
    clientSecret: 'c083a247e7a14bab9df99084f18f12ad',
});

let accessToken = '';

app.use(cors({ origin: '*' }));
app.listen(PORT, () => console.log(`Server started on port ${PORT}`));

app.get('/', (req, res) => {
    res.send('Hello World');
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
    returnData.sort((a, b) => new Date(a['dates']) - new Date(b['dates']));
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
});

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
});

app.get('/event/:id', async (req, res) => {
    let id = req.params['id'];
    let url = `https://app.ticketmaster.com/discovery/v2/events/${id}.json?apikey=${apikey}`;
    // console.log(url);
    try {
        const response = await axios.get(url);
        if ('name' in response.data) {
            res.status(200).json(response.data);
        }
        else {
            res.status(200).json(null);
        }
    } catch (error) {
        console.log(error);
        res.status(500).json(null);
    }
});

app.get('/venue/:name', async (req, res) => {
    let venueName = req.params['name'];
    let url = `https://app.ticketmaster.com/discovery/v2/venues.json?apikey=${apikey}&keyword=${venueName}`;
    try {
        const response = await axios.get(url);
        if ('_embedded' in response.data && 'venues' in response.data['_embedded']) {
            let venueDetails = response.data['_embedded']['venues'][0];
            res.status(200).json(venueDetails);
        }
        else {
            res.status(200).json(null);
        }
    } catch (error) {
        console.log(error);
        res.status(500).json(null);
    }
});


function getAccessTokenAndSearchKeyword(keyword, res) {
    spotifyApi.clientCredentialsGrant()
        .then(data => {
            accessToken = data.body['access_token'];
            spotifyApi.setAccessToken(accessToken);
            return spotifyApi.searchArtists(keyword);
        })
        .then(data => {
            const artistData = data?.['body']?.['artists']?.['items']?.[0];
            res.status(200).json(artistData)
        })
        .catch(err => {
            console.error(err);
            res.status(500).json(null);
        });
}

app.get('/spotifyArtist/:keyword', (req, res) => {
    const keyword = req.params['keyword'];
    // console.log(spotifyApi.getAccessToken());
    if (accessToken !== '' && spotifyApi.getAccessToken() !== '') {
        // Access token is still valid
        // console.log('Access token is still valid');
        spotifyApi.setAccessToken(accessToken);
        spotifyApi.searchArtists(keyword)
            .then(data => {
                const artistData = data?.['body']?.['artists']?.['items']?.[0];
                res.status(200).json(artistData)
            })
            .catch(err => {
                console.error(err);
                if (err.statusCode === 401) {
                    // Access token is expired
                    getAccessTokenAndSearchKeyword(keyword, res);
                }
                else {
                    res.status(500).json(null);
                }
            });
    } else {
        // Access token needs to be refreshed
        getAccessTokenAndSearchKeyword(keyword, res);
    }
});

function getAccessTokenAndSearchId(id, res) {
    spotifyApi.clientCredentialsGrant()
        .then(data => {
            accessToken = data.body['access_token'];
            spotifyApi.setAccessToken(accessToken);
            return spotifyApi.getArtistAlbums(id, {limit: 3});
        })
        .then(data => {
            const albumData = data?.['body']?.['items'];
            res.status(200).json(albumData)
        })
        .catch(err => {
            console.error(err);
            res.status(500).json(null);
        });
}
app.get('/spotifyAlbum/:id', (req, res) => {
    const id = req.params['id'];
    console.log("getting albums for id: " + id);
    if (accessToken !== '' && spotifyApi.getAccessToken() !== '') {
        // Access token is still valid
        // console.log('Access token is still valid');
        spotifyApi.setAccessToken(accessToken);
        spotifyApi.getArtistAlbums(id, { limit: 3 })
            .then(data => {
                const albumData = data?.['body']?.['items'];
                res.status(200).json(albumData)
            })
            .catch(err => {
                console.error(err);
                if (err.statusCode === 401) {
                    // Access token is expired
                    getAccessTokenAndSearchId(id, res);
                }
                else {
                    res.status(500).json(null);
                }
            });
    } else {
        // Access token needs to be refreshed
        getAccessTokenAndSearchId(id, res);
    }
});