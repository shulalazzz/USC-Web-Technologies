import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ResultsTableDataService} from "../../services/results-table-data.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-results-table',
  templateUrl: './results-table.component.html',
  styleUrls: ['./results-table.component.css']
})
export class ResultsTableComponent implements OnInit{
  eventsData: any;
  backendEventDetailsUrl: string = 'http://localhost:5000/event/';
  @Output() eventClicked = new EventEmitter<any>();

  constructor(private resultsTableDataService: ResultsTableDataService, private http: HttpClient) {
  }

  onEventClicked(event: any) {
    // console.log(event);
    this.http.get<any>(this.backendEventDetailsUrl + event.id).subscribe((data: any) => {
      // console.log(data);
      this.eventClicked.emit(data);
    })
  }

  ngOnInit() {
    this.resultsTableDataService.getData().subscribe((data: any) => {this.eventsData = data})
  }
}
