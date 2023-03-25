import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'app-event-details-card',
  templateUrl: './event-details-card.component.html',
  styleUrls: ['./event-details-card.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class EventDetailsCardComponent implements OnInit{
  @Input() event: any;
  @Output() backButtonClicked = new EventEmitter<boolean>();

  artistsArr: any = [];



  ngOnInit() {
    // console.log('from event-details-card.component.ts ngOnInit()' );
    // console.log(this.event);
    this.event = {
      "name": "The Batman in Concert",
      "type": "event",
      "id": "vvG10Z94XNA3TX",
      "test": false,
      "url": "https://www.ticketmaster.com/the-batman-in-concert-hollywood-california-04-15-2023/event/09005D5FA3152FFE",
      "locale": "en-us",
      "images": [
        {
          "ratio": "16_9",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RETINA_LANDSCAPE_16_9.jpg",
          "width": 1136,
          "height": 639,
          "fallback": false
        },
        {
          "ratio": "3_2",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RETINA_PORTRAIT_3_2.jpg",
          "width": 640,
          "height": 427,
          "fallback": false
        },
        {
          "ratio": "16_9",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_TABLET_LANDSCAPE_16_9.jpg",
          "width": 1024,
          "height": 576,
          "fallback": false
        },
        {
          "ratio": "3_2",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_TABLET_LANDSCAPE_3_2.jpg",
          "width": 1024,
          "height": 683,
          "fallback": false
        },
        {
          "ratio": "16_9",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RETINA_PORTRAIT_16_9.jpg",
          "width": 640,
          "height": 360,
          "fallback": false
        },
        {
          "ratio": "16_9",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_TABLET_LANDSCAPE_LARGE_16_9.jpg",
          "width": 2048,
          "height": 1152,
          "fallback": false
        },
        {
          "ratio": "16_9",
          "url": "https://s1.ticketm.net/dbimages/449692a.jpg",
          "width": 205,
          "height": 115,
          "fallback": false
        },
        {
          "ratio": "16_9",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RECOMENDATION_16_9.jpg",
          "width": 100,
          "height": 56,
          "fallback": false
        },
        {
          "ratio": "4_3",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_CUSTOM.jpg",
          "width": 305,
          "height": 225,
          "fallback": false
        },
        {
          "ratio": "3_2",
          "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_ARTIST_PAGE_3_2.jpg",
          "width": 305,
          "height": 203,
          "fallback": false
        }
      ],
      "sales": {
        "public": {
          "startDateTime": "2022-11-11T18:00:00Z",
          "startTBD": false,
          "startTBA": false,
          "endDateTime": "2023-04-16T02:30:00Z"
        },
        "presales": [
          {
            "startDateTime": "2022-11-09T18:00:00Z",
            "endDateTime": "2022-11-11T07:59:00Z",
            "name": "Dolby Theatre Presale"
          }
        ]
      },
      "dates": {
        "start": {
          "localDate": "2023-04-15",
          "localTime": "19:30:00",
          "dateTime": "2023-04-16T02:30:00Z",
          "dateTBD": false,
          "dateTBA": false,
          "timeTBA": false,
          "noSpecificTime": false
        },
        "timezone": "America/Los_Angeles",
        "status": {
          "code": "onsale"
        },
        "spanMultipleDays": false
      },
      "classifications": [
        {
          "primary": true,
          "segment": {
            "id": "KZFzniwnSyZfZ7v7nn",
            "name": "Film"
          },
          "genre": {
            "id": "KnvZfZ7vAke",
            "name": "Action/Adventure"
          },
          "subGenre": {
            "id": "KZazBEonSMnZfZ7vFlF",
            "name": "Action/Adventure"
          },
          "type": {
            "id": "KZAyXgnZfZ7v7nI",
            "name": "Undefined"
          },
          "subType": {
            "id": "KZFzBErXgnZfZ7v7lJ",
            "name": "Undefined"
          },
          "family": false
        }
      ],
      "promoter": {
        "id": "494",
        "name": "PROMOTED BY VENUE",
        "description": "PROMOTED BY VENUE / NTL / USA"
      },
      "promoters": [
        {
          "id": "494",
          "name": "PROMOTED BY VENUE",
          "description": "PROMOTED BY VENUE / NTL / USA"
        }
      ],
      "info": "YOU HEREBY RELEASE, COVENANT NOT TO SUE, DISCHARGE, AND HOLD HARMLESS THE THEATRE, ITS EMPLOYEES, AGENTS, AND REPRESENTATIVES, OF AND FROM ANY COVID-19 RELATED INJURY INCLUDING, BUT NOT LIMITED TO, PERSONAL INJURY, ILLNESS, DISABILITY, AND DEATH AND ANY ASSOCIATED DAMAGE, LOSS, CLAIM, LIABILITY, OR EXPENSE, OF ANY KIND (\"CLAIMS\"). YOU UNDERSTAND AND AGREE THAT THIS RELEASE INCLUDES ANY CLAIMS BASED ON THE NEGLIGENT ACTS OR OMISSIONS OF THE THEATRE, ITS EMPLOYEES, AGENTS, AND REPRESENTATIVES, WHETHER A COVID-19 INJURY OCCURS BEFORE, DURING OR AFTER YOUR VISIT TO THE THEATRE.",
      "pleaseNote": "ASSUMPTION OF RISK AND WAIVER OF LIABILITY RELATING TO CORONAVIRUS/COVID-19. COVID-19 is a highly contagious disease that can lead to severe illness and death. An inherent risk of exposure to COVID-19 exists in any place where people gather, including theatres. The Dolby Theatre (the \"Theatre\"), in compliance with guidelines issued by federal, state, and local governments, has put in place preventative measures to reduce the spread of COVID-19 in the Theatre. Notwithstanding these preventative measures, visiting the Theatre could increase your risk of contracting COVID-19 including from patrons, guests and other individuals in the Theatre. By purchasing this ticket and/or accessing the theatre, you acknowledge the highly contagious nature of COVID-19 and VOLUNTARILY ASSUME THE RISK THAT YOU MAY BE EXPOSED TO OR INFECTED BY COVID-19 BY ENTERING THE THEATRE AND THAT SUCH EXPOSURE OR INFECTION MAY RESULT IN PERSONAL INJURY, ILLNESS, PERMANENT DISABILITY, AND/OR DEATH.",
      "priceRanges": [
        {
          "type": "standard",
          "currency": "USD",
          "min": 40.0,
          "max": 115.0
        }
      ],
      "products": [
        {
          "name": "PARKWHIZ DOLBY THEATRE",
          "id": "vvG10Z9pxevobw",
          "url": "https://www.ticketmaster.com/parkwhiz-dolby-theatre-hollywood-california-04-15-2023/event/09005D6882051632",
          "type": "Upsell",
          "classifications": [
            {
              "primary": true,
              "segment": {
                "id": "KZFzniwnSyZfZ7v7n1",
                "name": "Miscellaneous"
              },
              "genre": {
                "id": "KnvZfZ7v7ll",
                "name": "Undefined"
              },
              "subGenre": {
                "id": "KZazBEonSMnZfZ7vAv1",
                "name": "Undefined"
              },
              "type": {
                "id": "KZAyXgnZfZ7vAva",
                "name": "Parking"
              },
              "subType": {
                "id": "KZFzBErXgnZfZ7vAFe",
                "name": "Regular"
              },
              "family": false
            }
          ]
        }
      ],
      "seatmap": {
        "staticUrl": "https://maps.ticketmaster.com/maps/geometry/3/event/09005D5FA3152FFE/staticImage?type=png&systemId=HOST"
      },
      "accessibility": {
        "ticketLimit": 2
      },
      "ageRestrictions": {
        "legalAgeEnforced": false
      },
      "ticketing": {
        "safeTix": {
          "enabled": true
        }
      },
      "_links": {
        "self": {
          "href": "/discovery/v2/events/vvG10Z94XNA3TX?locale=en-us"
        },
        "attractions": [
          {
            "href": "/discovery/v2/attractions/K8vZ917_yUV?locale=en-us"
          }
        ],
        "venues": [
          {
            "href": "/discovery/v2/venues/KovZpZAdtaEA?locale=en-us"
          }
        ]
      },
      "_embedded": {
        "venues": [
          {
            "name": "Dolby Theatre",
            "type": "venue",
            "id": "KovZpZAdtaEA",
            "test": false,
            "url": "https://www.ticketmaster.com/dolby-theatre-tickets-hollywood/venue/74167",
            "locale": "en-us",
            "aliases": [
              "kodac theater",
              "kodak theatre",
              "kodac theatre",
              "kodak theater"
            ],
            "images": [
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dbimages/14319v.jpg",
                "width": 205,
                "height": 115,
                "fallback": false
              }
            ],
            "postalCode": "90028",
            "timezone": "America/Los_Angeles",
            "city": {
              "name": "Hollywood"
            },
            "state": {
              "name": "California",
              "stateCode": "CA"
            },
            "country": {
              "name": "United States Of America",
              "countryCode": "US"
            },
            "address": {
              "line1": "6801 Hollywood Blvd."
            },
            "location": {
              "longitude": "-118.34020235",
              "latitude": "34.10177237"
            },
            "markets": [
              {
                "name": "Los Angeles",
                "id": "27"
              }
            ],
            "dmas": [
              {
                "id": 223
              },
              {
                "id": 324
              },
              {
                "id": 354
              },
              {
                "id": 383
              }
            ],
            "boxOfficeInfo": {
              "phoneNumberDetail": "Dolby Theatre Administration: 323-308-6300",
              "openHoursDetail": "NON-EVENT DAYS: Monday-Saturday 10am-5pm, Sunday 10am-4pm EVENT DAYS: Monday-Sunday 10am- 1/2 hour after scheduled start time of event.",
              "acceptedPaymentDetail": "Cash, Visa, MasterCard, American Express and Discover. No checks are accepted.",
              "willCallDetail": "Will Call is available for pickup during regular box office hours. Photo ID and credit card used for the purchase are required."
            },
            "parkingDetail": "2,000 parking spaces within a 10 minute walk, plus 3,000 parking spaces at Hollywood & Highland Center. For parking information visit: http://maps.google.com/maps?hl=en&gl=us&daddr=6801+Hollywood+Blvd,+Hollywood,+CA+90028",
            "accessibleSeatingDetail": "Accessible seating is available by phone through Ticketmaster or Dolby Theatre. There are elevators to each level, however only the seats indicated on the seating chart are without stairs and have space to accommodate wheelchairs and motorized carts.",
            "generalInfo": {
              "generalRule": "The Dolby Theatre is a non-smoking facility. No outside foods or beverages. No cameras or recording devices are allowed. Rules are subject to change for some events.",
              "childRule": "All guests require a ticket, regardless of age. Some events, rules subject to change and may not be suitable for children. Also disruptions to other guests experience may be cause to be asked to step out with no refunds or exchanges."
            },
            "upcomingEvents": {
              "_total": 46,
              "ticketmaster": 46,
              "_filtered": 0
            },
            "_links": {
              "self": {
                "href": "/discovery/v2/venues/KovZpZAdtaEA?locale=en-us"
              }
            }
          }
        ],
        "attractions": [
          {
            "name": "The Batman",
            "type": "attraction",
            "id": "K8vZ917_yUV",
            "test": false,
            "url": "https://www.ticketmaster.com.au/the-batman-tickets/artist/2844657",
            "locale": "en-us",
            "images": [
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RETINA_LANDSCAPE_16_9.jpg",
                "width": 1136,
                "height": 639,
                "fallback": false
              },
              {
                "ratio": "3_2",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RETINA_PORTRAIT_3_2.jpg",
                "width": 640,
                "height": 427,
                "fallback": false
              },
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_TABLET_LANDSCAPE_16_9.jpg",
                "width": 1024,
                "height": 576,
                "fallback": false
              },
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_EVENT_DETAIL_PAGE_16_9.jpg",
                "width": 205,
                "height": 115,
                "fallback": false
              },
              {
                "ratio": "3_2",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_TABLET_LANDSCAPE_3_2.jpg",
                "width": 1024,
                "height": 683,
                "fallback": false
              },
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RETINA_PORTRAIT_16_9.jpg",
                "width": 640,
                "height": 360,
                "fallback": false
              },
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_TABLET_LANDSCAPE_LARGE_16_9.jpg",
                "width": 2048,
                "height": 1152,
                "fallback": false
              },
              {
                "ratio": "16_9",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_RECOMENDATION_16_9.jpg",
                "width": 100,
                "height": 56,
                "fallback": false
              },
              {
                "ratio": "4_3",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_CUSTOM.jpg",
                "width": 305,
                "height": 225,
                "fallback": false
              },
              {
                "ratio": "3_2",
                "url": "https://s1.ticketm.net/dam/a/349/649a83dd-bd56-48a6-83c9-ea30f76b5349_1608431_ARTIST_PAGE_3_2.jpg",
                "width": 305,
                "height": 203,
                "fallback": false
              }
            ],
            "classifications": [
              {
                "primary": true,
                "segment": {
                  "id": "KZFzniwnSyZfZ7v7nn",
                  "name": "Film"
                },
                "genre": {
                  "id": "KnvZfZ7vAke",
                  "name": "Action/Adventure"
                },
                "subGenre": {
                  "id": "KZazBEonSMnZfZ7vFlF",
                  "name": "Action/Adventure"
                },
                "type": {
                  "id": "KZAyXgnZfZ7v7nI",
                  "name": "Undefined"
                },
                "subType": {
                  "id": "KZFzBErXgnZfZ7v7lJ",
                  "name": "Undefined"
                },
                "family": false
              }
            ],
            "upcomingEvents": {
              "_total": 3,
              "ticketmaster": 3,
              "_filtered": 0
            },
            "_links": {
              "self": {
                "href": "/discovery/v2/attractions/K8vZ917_yUV?locale=en-us"
              }
            }
          }
        ]
      }
    }
    if (this.event) {
      if (this.event.hasOwnProperty('_embedded') && this.event['_embedded'].hasOwnProperty('attractions')) {
        for (let i = 0; i < this.event['_embedded']['attractions'].length; i++) {
          this.artistsArr.push(this.event['_embedded']['attractions'][i]['name']);
        }
      }
    }
  }

  onBackButtonClicked() {
    this.backButtonClicked.emit(false);
  }

}
