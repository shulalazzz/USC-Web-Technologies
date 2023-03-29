import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() { }

  getLocalStorageItem(key: string): any {
    const str = localStorage.getItem(key);
    return str ? JSON.parse(str) : null;
  }

  setLocalStorageItem(key: string, value: any): void {
    localStorage.setItem(key, JSON.stringify(value));
  }

  removeLocalStorageItem(key: string): void {
    localStorage.removeItem(key);
  }

  containsLocalStorageItem(key: string): boolean {
    return localStorage.getItem(key) !== null;
  }

  convertToSortedArray(): any {
    console.log("localStorage: ", localStorage)
    const events: any[] = Object.keys(localStorage).filter(key => key !== "length" && key !== "debug").map(key => {
      const obj = localStorage.getItem(key) ?? '{}';
      return JSON.parse(obj);
    });
    events.sort((a, b) => new Date(a.curDate).getTime() - new Date(b.curDate).getTime());
    return events;
  }
}
