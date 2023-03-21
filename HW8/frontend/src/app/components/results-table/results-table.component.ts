import {Component, OnInit} from '@angular/core';
import {ResultsTableDataService} from "../../services/results-table-data.service";

@Component({
  selector: 'app-results-table',
  templateUrl: './results-table.component.html',
  styleUrls: ['./results-table.component.css']
})
export class ResultsTableComponent implements OnInit{
  eventData: any;

  constructor(private resultsTableDataService: ResultsTableDataService) {
  }

  onRowClicked(event: any) {
    console.log(event);
  }

  ngOnInit() {
    this.resultsTableDataService.getData().subscribe((data: any) => {this.eventData = data})
  }
}
