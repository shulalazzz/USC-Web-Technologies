import { TestBed } from '@angular/core/testing';

import { ResultsTableDataService } from './results-table-data.service';

describe('ResultsTableDataService', () => {
  let service: ResultsTableDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResultsTableDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
