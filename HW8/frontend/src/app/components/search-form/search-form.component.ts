import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {debounceTime, distinctUntilChanged, filter, finalize, switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent implements OnInit{

  myForm: FormGroup;
  isRequiredLocationText: boolean = true;
  autoCompleteKeyword: string = '';
  autoCompleteCtrl = new FormControl();
  isLoading = false;
  suggestions: any;

  private ipInfoApi: string = "https://ipinfo.io/json?token=fcee7187512c64";


  constructor(private http: HttpClient) {
    this.myForm = new FormGroup({
      keyword: new FormControl('', Validators.required),
      distance: new FormControl(10),
      category: new FormControl('Default'),
      locationText: new FormControl('', Validators.required),
      locationCheckbox: new FormControl(false)
    });
  }

  onClear() {
    this.myForm.reset()
    this.myForm.controls['distance'].setValue(10);
    this.myForm.controls['category'].setValue('Default');
    this.myForm.controls['locationCheckbox'].setValue(false);
    this.myForm.controls['locationText'].enable();
    // console.log(this.myForm.value);
    this.isRequiredLocationText = true;
    this.autoCompleteKeyword = '';
    this.suggestions = [];
  }

  onChangeLocationCheckbox(event: any) {
    if (event.target.checked) {
      this.myForm.controls['locationText'].clearValidators();
      this.myForm.controls['locationText'].setValue('');
      this.myForm.controls['locationText'].disable()
      this.isRequiredLocationText = false;
    }
    else {
      this.myForm.controls['locationText'].setValidators([Validators.required]);
      this.myForm.controls['locationText'].enable();
      this.isRequiredLocationText = true;
    }
    this.myForm.controls['locationText'].updateValueAndValidity();
    // console.log(this.myForm.controls['locationText'].valid);
  }

  onSubmit() {
    if (this.myForm.valid) {
      if (this.myForm.controls['locationCheckbox'].value === true) {
        this.http.get<any>(this.ipInfoApi).subscribe(response => {
          // console.log(response);
          let location :string = response.loc;
          // console.log(location);
          this.sendToBackendSearch(location);
        }, error => console.log(error));
      }
      else if (this.myForm.controls['locationCheckbox'].value === false) {
        let location :string = this.myForm.controls['locationText'].value.split(' ').join('+');
        this.http.get<any>(`https://maps.googleapis.com/maps/api/geocode/json?address=${location}&key=AIzaSyAhrUOOniYwPz_aLnuKi2M6v3DfG50oH5o`).subscribe(response => {
          // console.log(response);
          if (response.status === "ZERO_RESULTS") {
            alert("Invalid location");
            return;
          }
          let location :string = response.results[0].geometry.location.lat + ',' + response.results[0].geometry.location.lng;
          // console.log(location);
          this.sendToBackendSearch(location);
        }, error => console.log(error));
      }
    }
    else {
      console.log('Form is not valid');
    }
  }

  sendToBackendSearch(location: string) {
    this.myForm.value['location'] = location;
    const params = JSON.stringify(this.myForm.value);
    const url = 'http://localhost:5000/search/' + params;
    this.http.get<any>(url).subscribe(response => {
      console.log("from frontend");
      console.log(response);
    }, error => console.log(error));
  }

  ngOnInit() {
    this.autoCompleteCtrl.valueChanges.pipe(
      filter(res => {return res !== null && res.length >= 2}),
      distinctUntilChanged(),
      debounceTime(1000),
      tap(() => {
        this.suggestions = [];
        this.isLoading = true;
      }),
      switchMap(values => this.http.get('http://localhost:5000/autocomplete/' + values).pipe(
        finalize(() => {
          this.isLoading = false
        }),
        )
      )
    ).subscribe((data: any) => {
      if (data === null) {
        this.suggestions = [];
      }
      else {
        this.suggestions = data;
      }
    })
  }

}
