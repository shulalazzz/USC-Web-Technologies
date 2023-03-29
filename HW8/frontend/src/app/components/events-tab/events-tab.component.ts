import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-events-tab',
  templateUrl: './events-tab.component.html',
  styleUrls: ['./events-tab.component.css']
})
export class EventsTabComponent implements OnInit{
  @Input() event: any = {};

  // 0 for plain text, 1 for link, 2 for status, 3 for map, 4 for text expand
  dateData: any = {};
  artistData: any = {};
  venueData: any = {};
  genreData: any = {};
  priceData: any = {};
  ticketStatusData: any = {};
  buyTicketData: any = {};
  mapData: any = {};
  artistsArr: any = [];
  shareLink: string = '';

  ngOnInit() {
    if (this.event) {
      if (this.event.hasOwnProperty('dates')) {
        let date = this.event['dates']['start']['localDate'];
        // let time = this.event['dates']['start'].hasOwnProperty('localTime') ? this.event['dates']['start']['localTime'] : '';
        this.dateData = { header: 'Date', content: date, type: 0 };
      }
      if (this.event.hasOwnProperty('_embedded') && this.event['_embedded'].hasOwnProperty('attractions')) {
        for (let i = 0; i < this.event['_embedded']['attractions'].length; i++) {
          this.artistsArr.push(this.event['_embedded']['attractions'][i]['name']);
        }
        this.artistData = { header: 'Artist/Teams', content: this.artistsArr.join(' | '), type: 0 };
      }
      if (this.event.hasOwnProperty('_embedded') && this.event['_embedded'].hasOwnProperty('venues')) {
        let venue = this.event['_embedded']['venues'][0]['name'];
        this.venueData = { header: 'Venue', content: venue, type: 0 };
      }
      if (this.event.hasOwnProperty('classifications')) {
        let text = []
        if (this.event['classifications'][0].hasOwnProperty('segment') && this.event['classifications'][0]['segment']['name'] !== 'Undefined') {
          text.push(this.event['classifications'][0]['segment']['name'])
        }
        if (this.event['classifications'][0].hasOwnProperty('genre') && this.event['classifications'][0]['genre']['name'] !== 'Undefined') {
          text.push(this.event['classifications'][0]['genre']['name'])
        }
        if (this.event['classifications'][0].hasOwnProperty('subGenre') && this.event['classifications'][0]['subGenre']['name'] !== 'Undefined') {
          text.push(this.event['classifications'][0]['subGenre']['name'])
        }
        if (this.event['classifications'][0].hasOwnProperty('type') && this.event['classifications'][0]['type']['name'] !== 'Undefined') {
          text.push(this.event['classifications'][0]['type']['name'])
        }
        if (this.event['classifications'][0].hasOwnProperty('subType') && this.event['classifications'][0]['subType']['name'] !== 'Undefined') {
          text.push(this.event['classifications'][0]['subType']['name'])
        }
        this.genreData = { header: 'Genre', content: text.join(' | '), type: 0 };
      }
      if (this.event.hasOwnProperty('priceRanges')) {
        let min = this.event['priceRanges'][0]['min']
        let max = this.event['priceRanges'][0]['max']
        let currency = this.event['priceRanges'][0]['currency']
        this.priceData = { header: 'Price Range', content: min + ' - ' + max + ' ' + currency, type: 0 };
      }
      if (this.event.hasOwnProperty('url')) {
        this.buyTicketData = { header: 'Buy Ticket At', content: `<a href="${this.event['url']}" target="_blank">Ticketmaster</a>`, type: 1 };
        this.shareLink = this.event['url'];
      }
      if (this.event.hasOwnProperty('dates') && this.event['dates'].hasOwnProperty('status')) {
        this.ticketStatusData = { header: 'Ticket Status', content: this.event['dates']['status']['code'], type: 2 };
      }
      if (this.event.hasOwnProperty('seatmap')) {
        this.mapData = { header: 'Seat Map', content: this.event['seatmap']['staticUrl'], type: 3};
      }
    }
  }

}
