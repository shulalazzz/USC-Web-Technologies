import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {forkJoin, Observable} from 'rxjs';

@Component({
  selector: 'app-artist-tab',
  templateUrl: './artist-tab.component.html',
  styleUrls: ['./artist-tab.component.css']
})
export class ArtistTabComponent implements OnInit {
  @Input() artists: any;
  imgUrl: string = '';
  popularity: number = 0;
  followers: string = '';
  spotifyLink: string = '';
  backendUrl: string = 'http://localhost:8080/';
  albums: any;
  currentArtist: any;

  constructor(private http: HttpClient) { }

  ngOnInit() {
    let artistData = {
      "external_urls": {
        "spotify": "https://open.spotify.com/artist/78rUTD7y6Cy67W1RVzYs7t"
      },
      "followers": {
        "href": null,
        "total": 1887525
      },
      "genres": [],
      "href": "https://api.spotify.com/v1/artists/78rUTD7y6Cy67W1RVzYs7t",
      "id": "78rUTD7y6Cy67W1RVzYs7t",
      "images": [
        {
          "height": 640,
          "url": "https://i.scdn.co/image/ab6761610000e5ebfd7a593cda27e19c8768edea",
          "width": 640
        },
        {
          "height": 320,
          "url": "https://i.scdn.co/image/ab67616100005174fd7a593cda27e19c8768edea",
          "width": 320
        },
        {
          "height": 160,
          "url": "https://i.scdn.co/image/ab6761610000f178fd7a593cda27e19c8768edea",
          "width": 160
        }
      ],
      "name": "PinkPantheress",
      "popularity": 85,
      "type": "artist",
      "uri": "spotify:artist:78rUTD7y6Cy67W1RVzYs7t"
    }
    this.imgUrl = artistData?.['images']?.[0]?.['url'];
    this.popularity = artistData?.['popularity'];
    this.followers = artistData?.['followers']?.['total'].toLocaleString();
    this.spotifyLink = artistData?.['external_urls']?.['spotify'];
    this.currentArtist = 'PinkPantheress';

     this.albums = [
      {
        "album_group": "album",
        "album_type": "album",
        "artists": [
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/78rUTD7y6Cy67W1RVzYs7t"
            },
            "href": "https://api.spotify.com/v1/artists/78rUTD7y6Cy67W1RVzYs7t",
            "id": "78rUTD7y6Cy67W1RVzYs7t",
            "name": "PinkPantheress",
            "type": "artist",
            "uri": "spotify:artist:78rUTD7y6Cy67W1RVzYs7t"
          }
        ],
        "external_urls": {
          "spotify": "https://open.spotify.com/album/3KP55PNM7vdlrIm1LavDzb"
        },
        "href": "https://api.spotify.com/v1/albums/3KP55PNM7vdlrIm1LavDzb",
        "id": "3KP55PNM7vdlrIm1LavDzb",
        "images": [
          {
            "height": 640,
            "url": "https://i.scdn.co/image/ab67616d0000b27329693f4909f888a42e56b603",
            "width": 640
          },
          {
            "height": 300,
            "url": "https://i.scdn.co/image/ab67616d00001e0229693f4909f888a42e56b603",
            "width": 300
          },
          {
            "height": 64,
            "url": "https://i.scdn.co/image/ab67616d0000485129693f4909f888a42e56b603",
            "width": 64
          }
        ],
        "name": "to hell with it (Remixes)",
        "release_date": "2022-01-28",
        "release_date_precision": "day",
        "total_tracks": 25,
        "type": "album",
        "uri": "spotify:album:3KP55PNM7vdlrIm1LavDzb"
      },
      {
        "album_group": "album",
        "album_type": "album",
        "artists": [
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/78rUTD7y6Cy67W1RVzYs7t"
            },
            "href": "https://api.spotify.com/v1/artists/78rUTD7y6Cy67W1RVzYs7t",
            "id": "78rUTD7y6Cy67W1RVzYs7t",
            "name": "PinkPantheress",
            "type": "artist",
            "uri": "spotify:artist:78rUTD7y6Cy67W1RVzYs7t"
          }
        ],
        "external_urls": {
          "spotify": "https://open.spotify.com/album/65YAjLCn7Jp33nJpOxIPMe"
        },
        "href": "https://api.spotify.com/v1/albums/65YAjLCn7Jp33nJpOxIPMe",
        "id": "65YAjLCn7Jp33nJpOxIPMe",
        "images": [
          {
            "height": 640,
            "url": "https://i.scdn.co/image/ab67616d0000b27312c2eefb1d28509c632d915d",
            "width": 640
          },
          {
            "height": 300,
            "url": "https://i.scdn.co/image/ab67616d00001e0212c2eefb1d28509c632d915d",
            "width": 300
          },
          {
            "height": 64,
            "url": "https://i.scdn.co/image/ab67616d0000485112c2eefb1d28509c632d915d",
            "width": 64
          }
        ],
        "name": "to hell with it",
        "release_date": "2021-10-15",
        "release_date_precision": "day",
        "total_tracks": 10,
        "type": "album",
        "uri": "spotify:album:65YAjLCn7Jp33nJpOxIPMe"
      },
      {
        "album_group": "single",
        "album_type": "single",
        "artists": [
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/78rUTD7y6Cy67W1RVzYs7t"
            },
            "href": "https://api.spotify.com/v1/artists/78rUTD7y6Cy67W1RVzYs7t",
            "id": "78rUTD7y6Cy67W1RVzYs7t",
            "name": "PinkPantheress",
            "type": "artist",
            "uri": "spotify:artist:78rUTD7y6Cy67W1RVzYs7t"
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/3LZZPxNDGDFVSIPqf4JuEf"
            },
            "href": "https://api.spotify.com/v1/artists/3LZZPxNDGDFVSIPqf4JuEf",
            "id": "3LZZPxNDGDFVSIPqf4JuEf",
            "name": "Ice Spice",
            "type": "artist",
            "uri": "spotify:artist:3LZZPxNDGDFVSIPqf4JuEf"
          }
        ],
        "external_urls": {
          "spotify": "https://open.spotify.com/album/6cVfHBcp3AdpYY0bBglkLN"
        },
        "href": "https://api.spotify.com/v1/albums/6cVfHBcp3AdpYY0bBglkLN",
        "id": "6cVfHBcp3AdpYY0bBglkLN",
        "images": [
          {
            "height": 640,
            "url": "https://i.scdn.co/image/ab67616d0000b27342c5ba689b2e7cbc208a8fa7",
            "width": 640
          },
          {
            "height": 300,
            "url": "https://i.scdn.co/image/ab67616d00001e0242c5ba689b2e7cbc208a8fa7",
            "width": 300
          },
          {
            "height": 64,
            "url": "https://i.scdn.co/image/ab67616d0000485142c5ba689b2e7cbc208a8fa7",
            "width": 64
          }
        ],
        "name": "Boy's a liar Pt. 2",
        "release_date": "2023-02-03",
        "release_date_precision": "day",
        "total_tracks": 2,
        "type": "album",
        "uri": "spotify:album:6cVfHBcp3AdpYY0bBglkLN"
      }
    ]
  };

  getArtistData(artists: string[]) {
    if (artists.length == 0) {
      return null;
    }
    const requests = artists.map(artist => this.http.get<any>(this.backendUrl + "spotifyArtist/" + artist));
    return forkJoin(requests);
  }

  // getAlbum(id: string) {
  //   return this.http.get<any>(this.backendUrl + "spotifyAlbum/" + id);
  // }



}
