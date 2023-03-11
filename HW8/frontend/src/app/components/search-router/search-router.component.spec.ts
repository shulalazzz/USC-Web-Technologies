import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchRouterComponent } from './search-router.component';

describe('SearchRouterComponent', () => {
  let component: SearchRouterComponent;
  let fixture: ComponentFixture<SearchRouterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchRouterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchRouterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
