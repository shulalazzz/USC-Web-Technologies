import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent {

  myForm: FormGroup;
  isRequiredLocationText: boolean = true;
  private ipInfoApi: string = "https://ipinfo.io/json?token=fcee7187512c64";

  constructor(private http: HttpClient) {
    this.myForm = new FormGroup({
      keyword: new FormControl('', Validators.required),
      distance: new FormControl(10),
      category: new FormControl('default'),
      locationText: new FormControl('', Validators.required),
      locationCheckbox: new FormControl(false)
    });
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
        }, error => console.log(error));
      }
      else if (this.myForm.controls['locationCheckbox'].value === false) {
        let location :string = this.myForm.controls['locationText'].value.split(' ').join('+');
        this.http.get<any>(`https://maps.googleapis.com/maps/api/geocode/json?address=${location}&key=AIzaSyAhrUOOniYwPz_aLnuKi2M6v3DfG50oH5o`).subscribe(response => {
          // console.log(response);
          let location :string = response.results[0].geometry.location.lat + ',' + response.results[0].geometry.location.lng;
          // console.log(location);
        }, error => console.log(error));
      }
    }
    else {
      console.log('Form is not valid');
    }
  }


}
