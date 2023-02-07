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

function sendToPython(keyword, location, distance, category) {
    let xhr = new XMLHttpRequest()
    let data = JSON.stringify({keyword: keyword, location: location, distance: distance, category: category})
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            console.log(xhr)
            console.log(xhr.response)
        }
    }
    xhr.open("GET", 'http://127.0.0.1:5000/search/' + data, true)
    xhr.send()
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
            location = data.results[0].geometry.location.lat + ',' + data.results[0].geometry.location.lng
            sendToPython(keyword, location, distance, category)
            // console.log(data)
        }).catch(err => {console.log(err)})

    }
    else {
        fetch("https://ipinfo.io/json?token=fcee7187512c64").then(response => response.json()).then(data => {
            location = data.loc
            // console.log(data)
            sendToPython(keyword, location, distance, category)
        }).catch(err => {console.log(err)})
    }
    console.log(keyword, location, distance, category)
    return true
}

