import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventsTabComponentComponent } from './events-tab-component.component';

describe('EventsTabComponentComponent', () => {
  let component: EventsTabComponentComponent;
  let fixture: ComponentFixture<EventsTabComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventsTabComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventsTabComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
