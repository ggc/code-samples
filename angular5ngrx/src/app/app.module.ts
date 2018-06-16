import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { MyAppComponent } from './app.component';
import { NgxsModule } from '@ngxs/store';
import { UserState } from '../state-management/user.state';

import { NgxsReduxDevtoolsPluginModule } from '@ngxs/devtools-plugin';

@NgModule({
    declarations: [
        MyAppComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        NgxsModule.forRoot([
           UserState
        ]),
        NgxsReduxDevtoolsPluginModule.forRoot({
            disabled: false
        })
    ],
    providers: [],
    bootstrap: [MyAppComponent]
})
export class AppModule { }
