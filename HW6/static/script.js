window.onload = function () {
    document.getElementById('main-form').addEventListener('submit',  (e) => {
        e.preventDefault()
    })
}

// Ipinfo API
// fcee7187512c64
// Google Maps API
// AIzaSyAhrUOOniYwPz_aLnuKi2M6v3DfG50oH5o
function clearForm() {
    document.getElementById('main-form').reset()
    resetResultTable()
    let resultPanel = document.getElementById('result-panel')
    resultPanel.style.display = 'none'
    let eventCard = document.getElementById('event-card')
    eventCard.style.display = 'none'
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
    let keyword = document.getElementById('keyword').value
    if (keyword === '') {
        return false
    }
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
        fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${location}&key=AIzaSyAhrUOOniYwPz_aLnuKi2M6v3DfG50oH5o`).
        then(response => response.json()).then(data => {
            // location = data.results[0].geometry.location.lat + ',' + data.results[0].geometry.location.lng
            location = data['results'][0]['geometry']['location']['lat'] + ',' + data['results'][0]['geometry']['location']['lng']
            sendToPython(keyword, location, distance, category)
            // console.log(data)
        }).catch(err => {console.log(err)})

    }
    else {
        fetch("https://ipinfo.io/json?token=fcee7187512c64").then(response => response.json()).then(data => {
            location = data['loc']
            // console.log(data)
            sendToPython(keyword, location, distance, category)
        }).catch(err => {console.log(err)})
    }
    console.log(keyword, location, distance, category)
    return true
}

function sendToPython(keyword, location, distance, category) {
    let xhr = new XMLHttpRequest()
    let data = JSON.stringify({keyword: keyword, location: location, distance: distance, category: category})
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            // console.log(xhr)
            // console.log(xhr.response)
            sessionStorage.setItem('data', xhr.response)
            sessionStorage.setItem('eventOrder', '0')
            sessionStorage.setItem('genreOrder', '0')
            sessionStorage.setItem('venueOrder', '0')
            showResultDefault()
        }
    }
    xhr.open("GET", 'http://127.0.0.1:5000/search/' + data, true)
    xhr.send()
}

function showResultDefault() {
    let data = JSON.parse(sessionStorage.getItem('data'))
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
        date.innerHTML = data[i]['dates']['start']['localDate'] + (data[i]['dates']['start'].hasOwnProperty('localTime') ? '<br>' + data[i]['dates']['start']['localTime'] : '')
        icon.innerHTML = `<img src="${data[i]['images'][0]['url']}" alt="Event Icon" width="70px" height="40px">`
        event.innerHTML = `<a href="#event-card" id="${data[i]['id']}" class="event-link" onclick="showEventCard(this.id)">${data[i]['name']}</a>`
        genre.innerHTML = data[i]['classifications'][0]['genre']['name']
        venue.innerHTML = data[i]['_embedded']['venues'][0]['name']
    }
}

function resetResultTable() {
    let table = document.getElementById('result-table')
    let rowCount = table.rows.length
    for (let i = 1; i < rowCount; i++) {
        table.deleteRow(1)
    }
}

function showEventCard(id) {
    console.log(id)

}