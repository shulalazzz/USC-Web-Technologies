import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-events-tab-component',
  templateUrl: './events-tab-component.component.html',
  styleUrls: ['./events-tab-component.component.css']
})
export class EventsTabComponentComponent implements OnInit{
  // 0 for plain text, 1 for link, 2 for status, 3 for map, 4 for text expand
  @Input() data: {header: string, content: string, type: number} = {
    header: '',
    content: '',
    type: 0
  };
  status: string = '';
  backgroundColor: string = '';
  isExpanded: boolean = false;
  toggleExpanded() {
    this.isExpanded = !this.isExpanded;
  }
  constructor() {
    this.data = {header: 'Events', content: 'This is the events tab', type: 0};
  }

  ngOnInit() {
    if (this.data['type'] === 2) {
      this.showTicketStatus(this.data['content']);
    }
    // if (this.data['type'] === 4) {
    //   console.log("from events tab component, type 4");
    //   console.log(this.data);
    // }
  }

  showTicketStatus(code: string): void {
    switch (code) {
      case 'onsale':
        this.status = 'On Sale'
        this.backgroundColor = '#00b300'
        break
      case 'offsale':
        this.status = 'Off Sale'
        this.backgroundColor = '#ff0000'
        break
      case 'rescheduled':
        this.status = 'Rescheduled'
        this.backgroundColor = '#ff9900'
        break
      case 'cancelled':
        this.status = 'Cancelled'
        this.backgroundColor = '#000000'
        break
      case 'postponed':
        this.status = 'Postponed'
        this.backgroundColor = '#ff9900'
        break
    }
  }

}
