window.onload = function () {
    document.getElementById('main-form').addEventListener('submit',  (e) => {
        e.preventDefault()
    })
}

// Ipinfo API
//
// Google Maps API
//
function clearForm() {
    resetResult()
    document.getElementById('main-form').reset()
    document.getElementById('location-check').checked = false
    document.getElementById('location-text').style.display = 'block'
    document.getElementById('location-text').required = true
    sessionStorage.clear()
}

function checkAutoDetection() {
    let checkBox = document.getElementById('location-check')
    let inputBox = document.getElementById('location-text')
    if (checkBox.checked) {
        inputBox.style.display = 'none'
        inputBox.required = false
    }
    else {
        inputBox.style.display = 'block'
        inputBox.required = true
    }
}
function search() {
    resetResult()
    let keyword = document.getElementById('keyword').value
    if (keyword === '') {
        return false
    }
    keyword = keyword.split(' ').join('+')
    let checkBox = document.getElementById('location-check')
    let location = null
    let distance = 10
    if (document.getElementById('distance').value !== '') {
        distance = document.getElementById('distance').value
    }
    let category = document.getElementById('category').value
    if (!checkBox.checked) {
        location = document.getElementById('location-text').value
        if (location === '') {
            return false
        }
        location = location.split(' ').join('+')
        fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${location}&key=`).
        then(response => response.json()).then(data => {
            // location = data.results[0].geometry.location.lat + ',' + data.results[0].geometry.location.lng
            if (data['status'] === 'ZERO_RESULTS') {
                let notFound = document.getElementById('not-found')
                notFound.style.display = 'block'
                return
            }
            location = data['results'][0]['geometry']['location']['lat'] + ',' + data['results'][0]['geometry']['location']['lng']
            sendToPythonSearch(keyword, location, distance, category)
            // console.log(data)
        }).catch(err => {console.log(err)})

    }
    else {
        fetch("https://ipinfo.io/json?token=").then(response => response.json()).then(data => {
            location = data['loc']
            // console.log(data)
            sendToPythonSearch(keyword, location, distance, category)
        }).catch(err => {console.log(err)})
    }
    console.log(keyword, location, distance, category)
    return true
}

function sendToPythonSearch(keyword, location, distance, category) {
    let xhr = new XMLHttpRequest()
    let data = JSON.stringify({keyword: keyword, location: location, distance: distance, category: category})
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            // console.log(xhr)
            // console.log(xhr.response)
            preprocessResponse(xhr.response)
        }
    }
    xhr.open("GET", 'http://127.0.0.1:5000/search/' + data, true)
    // xhr.open("GET", 'https://csci-571-hw6-376303.wl.r.appspot.com/search/' + data, true)
    xhr.send()
}

function preprocessResponse(response) {
    let data = JSON.parse(response)
    // console.log(data)
    if (data === null) {
        let notFound = document.getElementById('not-found')
        notFound.style.display = 'block'
        return
    }
    let processedData = []
    for (let i = 0; i < data.length; i++) {
        let event = {}
        if (data[i].hasOwnProperty('id')) {
            event['id'] = data[i]['id']
        }
        else continue
        if (data[i].hasOwnProperty('name')) {
            event['eventName'] = data[i]['name']
        }
        else continue
        if (data[i].hasOwnProperty('dates')) {
            event['dates'] = data[i]['dates']['start']['localDate'] + (data[i]['dates']['start'].hasOwnProperty('localTime') ? '<br>' + data[i]['dates']['start']['localTime'] : '')
        }
        else continue
        if (data[i].hasOwnProperty('images')) {
            event['images'] = data[i]['images'][0]['url']
        }
        else continue
        if (data[i].hasOwnProperty('classifications') && data[i]['classifications'].length > 0 && data[i]['classifications'][0].hasOwnProperty('segment')
            && data[i]['classifications'][0]['segment'].hasOwnProperty('name') && data[i]['classifications'][0]['segment']['name'] !== 'Undefined') {
            event['genre'] = data[i]['classifications'][0]['segment']['name']
        }
        else continue
        if (data[i].hasOwnProperty('_embedded') && data[i]['_embedded'].hasOwnProperty('venues') && data[i]['_embedded']['venues'].length > 0) {
            event['venue'] = data[i]['_embedded']['venues'][0]['name']
        }
        else continue
        processedData.push(event)
    }
    sessionStorage.setItem('data', JSON.stringify(processedData))
    // following attributes are used to sort the result
    sessionStorage.setItem('eventName', '0')
    sessionStorage.setItem('genre', '0')
    sessionStorage.setItem('venue', '0')
    showResult()
}

function showResult() {
    let data = JSON.parse(sessionStorage.getItem('data'))
    if (data === null) {
        console.log('No data in showResult')
        return
    }
    let resultPanel = document.getElementById('result-panel')
    resultPanel.style.display = 'block'
    let table = document.getElementById('result-table')
    for (let i = 0; i < data.length; i++) {
        let row = table.insertRow(-1)
        let date = row.insertCell(0)
        let icon = row.insertCell(1)
        let event = row.insertCell(2)
        let genre = row.insertCell(3)
        let venue = row.insertCell(4)
        date.innerHTML = data[i]['dates']
        icon.innerHTML = `<img src="${data[i]['images']}" alt="Event Icon" width="70px" height="40px">`
        event.innerHTML = `<a id="${data[i]['id']}" class="event-link" onclick="sendToPythonEventDetails(this.id)">${data[i]['eventName']}</a>`
        genre.innerHTML = data[i]['genre']
        venue.innerHTML = data[i]['venue']
    }
}

function resetResult() {
    let table = document.getElementById('result-table')
    let rowCount = table.rows.length
    for (let i = 1; i < rowCount; i++) {
        table.deleteRow(1)
    }
    let resultPanel = document.getElementById('result-panel')
    resultPanel.style.display = 'none'
    let eventCard = document.getElementById('event-card')
    eventCard.style.display = 'none'
    let venueDetailsArrowPart = document.getElementById('venue-details-arrow-part')
    venueDetailsArrowPart.style.display = 'none'
    let venueCard = document.getElementById('venue-card')
    venueCard.style.display = 'none'
    let notFound = document.getElementById('not-found')
    notFound.style.display = 'none'
}

function sortTable(attribute) {
    let data = JSON.parse(sessionStorage.getItem('data'))
    // console.log(data)
    if (data === null) {
        console.log('No data in sortTable')
        return
    }
    let order = sessionStorage.getItem(attribute)
    if (order === '0') {
        data.sort((a, b) => a[attribute] > b[attribute] ? 1 : -1)
        sessionStorage.setItem(attribute, '1')
    }
    else {
        data.sort((a, b) => a[attribute] < b[attribute] ? 1 : -1)
        sessionStorage.setItem(attribute, '0')
    }
    sessionStorage.setItem('data', JSON.stringify(data))
    resetResult()
    showResult()
}

function sendToPythonEventDetails(id) {
    let xhr = new XMLHttpRequest()
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            // console.log(xhr.response)
            showEventCard(xhr.response)
        }
    }
    xhr.open("GET", 'http://127.0.0.1:5000/event/' + id, true)
    // xhr.open("GET", 'https://csci-571-hw6-376303.wl.r.appspot.com/event/' + id, true)
    xhr.send()
}
function showEventCard(response) {
    let venueCard = document.getElementById('venue-card')
    venueCard.style.display = 'none'
    let data = JSON.parse(response)
    // console.log(data)
    if (data === null) return
    let eventCard = document.getElementById('event-card')
    eventCard.style.display = 'block'
    document.getElementById('event-card-title').innerHTML = data['name']
    let dateDiv = document.getElementById('event-card-date')
    if (data.hasOwnProperty('dates')) {
        dateDiv.style.display = 'block'
        let date = data['dates']['start']['localDate']
        let time = data['dates']['start'].hasOwnProperty('localTime') ? data['dates']['start']['localTime'] : ''
        dateDiv.lastElementChild.innerHTML = date + (time !== '' ? ' ' + time : '')
    }
    else {
        dateDiv.style.display = 'none'
    }
    let artDiv = document.getElementById('event-card-art')
    if (data.hasOwnProperty('_embedded') && data['_embedded'].hasOwnProperty('attractions')) {
        artDiv.style.display = 'block'
        let arr = []
        let cur = ''
        for (let i = 0; i < data['_embedded']['attractions'].length; i++) {
            cur = `<a class="event-card-link" href="${data['_embedded']['attractions'][i]['url']}" target="_blank">${data['_embedded']['attractions'][i]['name']}</a>`
            arr.push(cur)
        }
        artDiv.lastElementChild.innerHTML = arr.join(' | ')
    }
    else {
        artDiv.style.display = 'none'
    }
    let venueDiv = document.getElementById('event-card-venue')
    if (data.hasOwnProperty('_embedded') && data['_embedded'].hasOwnProperty('venues')) {
        venueDiv.style.display = 'block'
        venueDiv.lastElementChild.innerHTML = data['_embedded']['venues'][0]['name']
        let venueDetailsArrow = document.getElementById('venue-details-arrow')
        venueDetailsArrow.setAttribute('onclick', `sendToPythonVenueDetails('${data['_embedded']['venues'][0]['name']}')`)
    }
    else {
        venueDiv.style.display = 'none'
    }
    let genreDiv = document.getElementById('event-card-genre')
    if (data.hasOwnProperty('classifications')) {
        genreDiv.style.display = 'block'
        let text = []
        if (data['classifications'][0].hasOwnProperty('segment') && data['classifications'][0]['segment']['name'] !== 'Undefined') {
            text.push(data['classifications'][0]['segment']['name'])
        }
        if (data['classifications'][0].hasOwnProperty('genre') && data['classifications'][0]['genre']['name'] !== 'Undefined') {
            text.push(data['classifications'][0]['genre']['name'])
        }
        if (data['classifications'][0].hasOwnProperty('subGenre') && data['classifications'][0]['subGenre']['name'] !== 'Undefined') {
            text.push(data['classifications'][0]['subGenre']['name'])
        }
        if (data['classifications'][0].hasOwnProperty('type') && data['classifications'][0]['type']['name'] !== 'Undefined') {
            text.push(data['classifications'][0]['type']['name'])
        }
        if (data['classifications'][0].hasOwnProperty('subType') && data['classifications'][0]['subType']['name'] !== 'Undefined') {
            text.push(data['classifications'][0]['subType']['name'])
        }
        genreDiv.lastElementChild.innerHTML = text.join(' | ')
    }
    else {
        genreDiv.style.display = 'none'
    }
    let priceDiv = document.getElementById('event-card-price')
    if (data.hasOwnProperty('priceRanges')) {
        priceDiv.style.display = 'block'
        let min = data['priceRanges'][0]['min']
        let max = data['priceRanges'][0]['max']
        let currency = data['priceRanges'][0]['currency']
        priceDiv.lastElementChild.innerHTML = min + ' - ' + max + ' ' + currency
    }
    else {
        priceDiv.style.display = 'none'
    }
    let ticketStatusDiv = document.getElementById('event-card-ticket-status')
    if (data.hasOwnProperty('dates') && data['dates'].hasOwnProperty('status')) {
        ticketStatusDiv.style.display = 'block'
        showTicketStatus(data['dates']['status']['code'])
    }
    else {
        ticketStatusDiv.style.display = 'none'
    }
    let buyTicketDiv = document.getElementById('event-card-buy-ticket')
    if (data.hasOwnProperty('url')) {
        buyTicketDiv.style.display = 'block'
        buyTicketDiv.lastElementChild.innerHTML = `<a href="${data['url']}" target="_blank">Ticketmaster</a>`
    }
    else {
        buyTicketDiv.style.display = 'none'
    }
    let seatMapDiv = document.getElementById('event-card-map')
    if (data.hasOwnProperty('seatmap')) {
        seatMapDiv.style.display = 'block'
        document.getElementById('event-card-map').innerHTML = `<img class="seat-map" id="seat-map" src="${data['seatmap']['staticUrl']}" alt="seat map">`
    }
    else {
        seatMapDiv.style.display = 'none'
    }
    eventCard.scrollIntoView({behavior: "smooth"})
    let venueDetailsArrowPart = document.getElementById('venue-details-arrow-part')
    venueDetailsArrowPart.style.display = 'block'
}

function showTicketStatus(code) {
    let statusSpan = document.getElementById('event-card-ticket-status').lastElementChild
    let status = ''
    switch (code) {
        case 'onsale':
            status = 'On Sale'
            statusSpan.style.backgroundColor = '#00b300'
            break
        case 'offsale':
            status = 'Off Sale'
            statusSpan.style.backgroundColor = '#ff0000'
            break
        case 'rescheduled':
            status = 'Rescheduled'
            statusSpan.style.backgroundColor = '#ff9900'
            break
        case 'cancelled':
            status = 'Cancelled'
            statusSpan.style.backgroundColor = '#000000'
            break
        case 'postponed':
            status = 'Postponed'
            statusSpan.style.backgroundColor = '#ff9900'
            break
    }
    statusSpan.innerHTML = status
}

function sendToPythonVenueDetails(venueName) {
    let xhr = new XMLHttpRequest()
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            showVenueCard(xhr.response)
        }
    }
    xhr.open("GET", 'http://127.0.0.1:5000/venue/' + venueName.split(' ').join('+'), true)
    // xhr.open("GET", 'https://csci-571-hw6-376303.wl.r.appspot.com/venue/' + venueName.split(' ').join('+'), true)
    xhr.send()
}

function showVenueCard(response) {
    let venueDetailArrowPart = document.getElementById('venue-details-arrow-part')
    venueDetailArrowPart.style.display = 'none'
    let venueData = JSON.parse(response)
    // console.log(venueData)
    if (venueData === null) {
        document.getElementById('venue-card').style.display = 'none'
        return
    }
    document.getElementById('venue-card').style.display = 'block'
    document.getElementById('venue-card-title').innerHTML = venueData['name']
    let venueCardAddress = document.getElementById('venue-card-address')
    let addressHtmlText = ''
    let addressTexts = [venueData['name']]
    if (venueData.hasOwnProperty('address') && venueData['address'].hasOwnProperty('line1')) {

        addressHtmlText += `<p>${venueData['address']['line1']}</p>`
        addressTexts.push(venueData['address']['line1'])
    }
    if (venueData.hasOwnProperty('city') && venueData['city'].hasOwnProperty('name')) {
        addressHtmlText += `<p>${venueData['city']['name']}, `
        addressTexts.push(venueData['city']['name'])
    }
    if (venueData.hasOwnProperty('state') && venueData['state'].hasOwnProperty('stateCode')) {
        addressHtmlText += `${venueData['state']['stateCode']} </p>`
        addressTexts.push(venueData['state']['stateCode'])
    }
    if (venueData.hasOwnProperty('postalCode')) {
        addressHtmlText += `<p>${venueData['postalCode']}</p>`
        addressTexts.push(venueData['postalCode'])
    }
    venueCardAddress.innerHTML = addressHtmlText
    let venueCardGoogleMap = document.getElementById('venue-card-google-map')
    let link = 'https://www.google.com/maps/search/?api=1&query=' + addressTexts.join(',+')
    venueCardGoogleMap.setAttribute('href', link)

    if (venueData.hasOwnProperty('images') && venueData['images'].length > 0) {
        document.getElementById('venue-card-image').innerHTML = `<img src="${venueData['images'][0]['url']}" alt="venue image">`
    }
    else {
        document.getElementById('venue-card-image').innerHTML = ''
    }
    if (venueData.hasOwnProperty('url')) {
        document.getElementById('venue-card-more-events').setAttribute('href', venueData['url'])
    }
    else {
        document.getElementById('venue-card-more-events').removeAttribute('href')
    }


    let venueCard = document.getElementById('venue-card')
    venueCard.scrollIntoView({behavior: "smooth"})
}