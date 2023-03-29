import { Component, OnInit } from '@angular/core';
import {LocalStorageService} from "../../services/local-storage.service";

const API_KEY = "e8067b53"
@Component({
  selector: 'app-favorites-router',
  templateUrl: './favorites-router.component.html',
  styleUrls: ['./favorites-router.component.css']
})
export class FavoritesRouterComponent implements OnInit{

  events: any[] = [];
  noResultMsg: string = "No favorite events to show";
  constructor(private localStorageService: LocalStorageService) { }
  ngOnInit() {
    this.events = this.localStorageService.convertToSortedArray();
    console.log("from favorites router, events: ", this.events);
  }

  onDeleteClick(event: any) {
    alert("Removed from favorites!");
    this.localStorageService.removeLocalStorageItem(event.id);
    this.events = this.localStorageService.convertToSortedArray();
  }

}
