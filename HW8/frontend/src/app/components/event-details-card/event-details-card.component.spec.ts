import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventDetailsCardComponent } from './event-details-card.component';

describe('EventDetailsCardComponent', () => {
  let component: EventDetailsCardComponent;
  let fixture: ComponentFixture<EventDetailsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventDetailsCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
