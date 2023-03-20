const express = require('express')
const geoHash = require('ngeohash');
const axios = require('axios');
const cors = require('cors');

const app = express()

const apikey = 'pviXh7vSvsPRcVqdsl6D1b1deOhtRKcb'

const categoryMap = {'Music': 'KZFzniwnSyZfZ7v7nJ', 'Sports': 'KZFzniwnSyZfZ7v7nE', 'Arts': 'KZFzniwnSyZfZ7v7na',
    'Film': 'KZFzniwnSyZfZ7v7nn', 'Miscellaneous': 'KZFzniwnSyZfZ7v7n1', 'Default': ''}

app.use(cors({ origin: 'http://localhost:4200' }))

app.get('/', (req, res) => {
    console.log('Hello, world!')
    res.send('Hello, world!')
});
app.get('/search/:data', async (req, res) => {
    let data = JSON.parse(req.params['data'])
    let loc = data['location'].split(',')
    let lat = loc[0]
    let lng = loc[1]
    let geoPoint = geoHash.encode(lat, lng, 7)
    let radius = data['distance']
    let keyword = data['keyword'].split(' ').join('+')
    let unit = 'miles'
    let segmentId = categoryMap[data['category']]
    let url = `https://app.ticketmaster.com/discovery/v2/events.json?apikey=${apikey}&keyword=${keyword}&radius=${radius}&unit=${unit}&geoPoint=${geoPoint}&segmentId=${segmentId}`
    try {
        const response = await axios.get(url)
        console.log(response.data)
        if ('_embedded' in response.data && 'events' in response.data['_embedded']) {
            let events = response.data['_embedded']['events'].slice(0, 20)
            res.status(200).json(events)
        }
        else {
            res.status(200).json(null)
        }
    } catch (error) {
        res.status(500).json(null)
    }
})

app.get('/autocomplete/:keyword', async (req, res) => {
    let keyword = req.params['keyword']
    let url = `https://app.ticketmaster.com/discovery/v2/suggest?apikey=${apikey}&keyword=${keyword}`
    try {
        const response = await axios.get(url)
        if ('_embedded' in response.data && 'attractions' in response.data['_embedded']) {
            let attractions = response.data['_embedded']['attractions'].slice(0, 5)
            let suggestions = []
            attractions.forEach(attraction => {
                suggestions.push(attraction['name'])
            })
            // console.log(suggestions)
            res.status(200).json(suggestions)
        }
        else {
            res.status(200).json(null)
        }
    } catch (error) {
        res.status(500).json(null)
    }
})

const PORT = process.env.PORT || 5000

app.listen(PORT, () => console.log(`Server started on port ${PORT}`))
