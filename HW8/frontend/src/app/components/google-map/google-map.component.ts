import {Component, Input, OnChanges, SimpleChanges, ViewEncapsulation} from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-google-map',
  templateUrl: './google-map.component.html',
  styleUrls: ['./google-map.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class GoogleMapComponent implements OnChanges{

  @Input() centerPosition: any;
  mapOptions: any = {
    center: { lat: 38.9987208, lng: -77.2538699 },
    zoom : 14
  }
  marker = {
    position: { lat: 38.9987208, lng: -77.2538699 },
  }


  constructor(private modalService: NgbModal) {}

  open(content: any) {
    this.modalService.open(content, {backdrop: 'static'});
  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes['centerPosition']) {
      this.mapOptions.center = changes['centerPosition'].currentValue;
      this.marker.position = changes['centerPosition'].currentValue;
    }
  }
}
