import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { GoogleMapsModule } from '@angular/google-maps'

import { SearchFormComponent } from './components/search-form/search-form.component';
import { SearchRouterComponent } from './components/search-router/search-router.component';
import { FavoritesRouterComponent } from './components/favorites-router/favorites-router.component';
import { ResultsTableComponent } from './components/results-table/results-table.component';
import { NoResultsComponent } from './components/no-results/no-results.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { EventDetailsCardComponent } from './components/event-details-card/event-details-card.component';
import {MatTabsModule} from "@angular/material/tabs";
import { EventsTabComponent } from './components/events-tab/events-tab.component';
import { ArtistTabComponent } from './components/artist-tab/artist-tab.component';
import { VenueTabComponent } from './components/venue-tab/venue-tab.component';
import { EventsTabComponentComponent } from './components/events-tab-component/events-tab-component.component';
import { GoogleMapComponent } from './components/google-map/google-map.component';

const appRoutes: Routes = [
  {path: 'search', component: SearchRouterComponent},
  {path: 'favorites', component: FavoritesRouterComponent},
  {path: '**', redirectTo: '/search', pathMatch: 'full'},
  ]

@NgModule({
  declarations: [
    AppComponent,
    SearchRouterComponent,
    FavoritesRouterComponent,
    SearchFormComponent,
    ResultsTableComponent,
    NoResultsComponent,
    EventDetailsCardComponent,
    EventsTabComponent,
    ArtistTabComponent,
    VenueTabComponent,
    EventsTabComponentComponent,
    GoogleMapComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes, {enableTracing: true}),
    FormsModule,
    ReactiveFormsModule,
    MatTooltipModule,
    NgbModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule,
    MatTabsModule,
    GoogleMapsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
