import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ResultsTableDataService {

  private resultsTableData = new BehaviorSubject<any>({});

  setData(data: any) {
    this.resultsTableData.next(data);
  }

  getData(): any {
    return this.resultsTableData.asObservable();
  }
  constructor() { }
}
