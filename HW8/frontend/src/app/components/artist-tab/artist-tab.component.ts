import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, forkJoin, map, Observable, of} from 'rxjs';

@Component({
  selector: 'app-artist-tab',
  templateUrl: './artist-tab.component.html',
  styleUrls: ['./artist-tab.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ArtistTabComponent implements OnInit {
  @Input() artistNames: String[] = [];
  backendUrl: string = 'http://localhost:5000/';
  processedArtists: any = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    if (this.artistNames.length > 0) {
      // const requests = this.artists.map(artistName => fetch(this.backendUrl + "spotifyArtist/" + artistName));
      // Promise.all(requests).then(responses => Promise.all(responses.map(response => response.json()))).then(data => Promise.all(data.map(artistData => {
      //   let albums = fetch(this.backendUrl + "spotifyAlbum/" + artistData?.id).then(albumResponse => albumResponse.json());
      //   return {
      //     name: artistData?.name,
      //     imgUrl: artistData?.images[0]?.url,
      //     popularity: artistData.popularity,
      //     followers: artistData?.followers?.total.toLocaleString(),
      //     spotifyLink: artistData?.external_urls?.spotify,
      //     albums: albums,
      //   };
      // }))).then(data => {
      //   this.processedArtists = data;
      //   console.log(this.processedArtists);
      // });
      const fetchArtistPromises = this.artistNames.map(artistName => fetch(this.backendUrl + "spotifyArtist/" + artistName));
      const processedArtistsPromise = Promise.all(fetchArtistPromises)
        .then(responses => Promise.all(responses.map(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error(`API call failed with status ${response.status}`);
          }
        })))
        .then(artistDataArr => {
          const fetchAlbumPromises = artistDataArr.map(artistData => fetch(this.backendUrl + "spotifyAlbum/" + artistData?.id));
          return Promise.all(fetchAlbumPromises)
            .then(albumResponses => Promise.all(albumResponses.map(albumResponse => {
              if (albumResponse.ok) {
                return albumResponse.json();
              } else {
                throw new Error(`API call failed with status ${albumResponse.status}`);
              }
            })))
            .then(albumsArr => artistDataArr.map((artistData, i) => ({
              name: artistData?.name,
              imgUrl: artistData?.images[0]?.url,
              popularity: artistData.popularity,
              followers: artistData?.followers?.total.toLocaleString(),
              spotifyLink: artistData?.external_urls?.spotify,
              albums: albumsArr[i],
            })));
        })
        .catch(error => {
          console.error(error);
          return null; // or handle the error case appropriately
        });

      processedArtistsPromise.then(processedArtists => {
        if (processedArtists !== null) {
          this.processedArtists = processedArtists;
          // console.log(this.processedArtists);
        }
      });

    }
  };



}
