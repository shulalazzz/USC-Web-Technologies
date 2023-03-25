import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueTabComponent } from './venue-tab.component';

describe('VenueTabComponent', () => {
  let component: VenueTabComponent;
  let fixture: ComponentFixture<VenueTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VenueTabComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VenueTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
