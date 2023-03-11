import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoritesRouterComponent } from './favorites-router.component';

describe('FavoritesRouterComponent', () => {
  let component: FavoritesRouterComponent;
  let fixture: ComponentFixture<FavoritesRouterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoritesRouterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoritesRouterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
