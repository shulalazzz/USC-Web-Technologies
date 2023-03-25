import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtistTabComponent } from './artist-tab.component';

describe('ArtistTabComponent', () => {
  let component: ArtistTabComponent;
  let fixture: ComponentFixture<ArtistTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArtistTabComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArtistTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
