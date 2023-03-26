import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-venue-tab',
  templateUrl: './venue-tab.component.html',
  styleUrls: ['./venue-tab.component.css']
})
export class VenueTabComponent implements OnInit{
  @Input() venueName: any;
  backendVenueUrl: string = 'http://localhost:5000/venue/';
  venueLocation: any;
  // 0 for plain text, 1 for link, 2 for status, 3 for map, 4 for text expand
  nameData: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 0
  };
  addressData: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 0
  };
  phoneNumberData: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 0
  };


  openHoursData: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 4
  }

  generalRuleData: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 4
  }

  childRuleData: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 4
  }

  constructor(private http: HttpClient) {}
  ngOnInit() {
    this.sendToBackendVenue();
  }

  sendToBackendVenue() {
    this.venueName = this.venueName.split(' ').join('+');
    this.http.get<any>(this.backendVenueUrl + this.venueName).subscribe(data => {
      if (data.hasOwnProperty('name')) {
        this.setVenueData(data)
      }
    }, error => {console.log(error)});
  }

  setVenueData(data: any) {
    if (data) {
      this.nameData = {header: "Name", content: data.name, type: 0};
      let addressTexts = []
      if (data.hasOwnProperty('address') && data['address'].hasOwnProperty('line1')) {

        addressTexts.push(data['address']['line1'])
      }
      if (data.hasOwnProperty('city') && data['city'].hasOwnProperty('name')) {
        addressTexts.push(data['city']['name'])
      }
      if (data.hasOwnProperty('state') && data['state'].hasOwnProperty('stateCode')) {
        addressTexts.push(data['state']['stateCode'])
      }
      this.addressData = {header: "Address", content: addressTexts.join(', '), type: 0};
      if (data.hasOwnProperty('boxOfficeInfo') && data['boxOfficeInfo'].hasOwnProperty('phoneNumberDetail')) {
        this.phoneNumberData = {header: "Phone Number", content: data['boxOfficeInfo']['phoneNumberDetail'], type: 0};
      }
      if (data.hasOwnProperty('boxOfficeInfo') && data['boxOfficeInfo'].hasOwnProperty('openHoursDetail')) {
        this.openHoursData = {header: "Open Hours", content: data['boxOfficeInfo']['openHoursDetail'], type: 4};
      }
      if (data.hasOwnProperty('generalInfo') && data['generalInfo'].hasOwnProperty('generalRule')) {
        this.generalRuleData = {header: "General Rule", content: data['generalInfo']['generalRule'], type: 4};
      }
      if (data.hasOwnProperty('generalInfo') && data['generalInfo'].hasOwnProperty('childRule')) {
        this.childRuleData = {header: "Child Rule", content: data['generalInfo']['childRule'], type: 4};
      }
      if (data.hasOwnProperty('location') && data['location'].hasOwnProperty('latitude') && data['location'].hasOwnProperty('longitude')) {
        this.venueLocation = {lat: parseFloat(data['location']['latitude']), lng: parseFloat(data['location']['longitude'])}
        // console.log("from venue tab setVenueData");
        // console.log(this.venueLocation);
      }
    }
  }

}
