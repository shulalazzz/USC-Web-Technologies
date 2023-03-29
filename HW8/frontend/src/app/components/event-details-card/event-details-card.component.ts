import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {LocalStorageService} from "../../services/local-storage.service";

@Component({
  selector: 'app-event-details-card',
  templateUrl: './event-details-card.component.html',
  styleUrls: ['./event-details-card.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class EventDetailsCardComponent implements OnInit{
  @Input() event: any;
  @Output() backButtonClicked = new EventEmitter<boolean>();

  artistsArr: any = [];
  noMusicArtistMSG: string = 'No music related artist details to show';
  eventInFav: boolean = false;


  constructor(private localStorageService: LocalStorageService) { }
  ngOnInit() {
    if (this.event?.['_embedded']?.attractions?.length) {
      this.event['_embedded']['attractions'].forEach((attraction :any) => {
        if (attraction?.['classifications']?.[0]?.['segment']?.['name']?.includes('Music')) {
          this.artistsArr.push(attraction['name'] ?? '');
        }
      });
      // console.log("from event detail, found music artists: ", this.artistsArr);
      if (this.localStorageService.containsLocalStorageItem(this.event?.id)) {
        this.eventInFav = true;
      }
    }
  }

  onBackButtonClicked() {
    this.backButtonClicked.emit(false);
  }

  onFavoriteButtonClicked() {
    if (this.event && this.event.id) {
      if (this.localStorageService.containsLocalStorageItem(this.event.id)) {
        this.localStorageService.removeLocalStorageItem(this.event.id);
        this.eventInFav = false;
        alert('Event Removed from Favorites!');
      } else {
        let curDate = new Date().toISOString();
        let genre = '';
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
          genre = text.join(' | ');
        }
        let eventObj = {
          id: this.event?.id,
          date: this.event?.dates?.start?.localDate,
          eventName: this.event?.name,
          curDate: curDate,
          genre: genre,
          venue: this.event?._embedded?.venues[0]?.name,
        }
        this.localStorageService.setLocalStorageItem(this.event.id, eventObj);
        alert('Event Added to Favorites!');
        this.eventInFav = true;
      }
    }
  }

}
