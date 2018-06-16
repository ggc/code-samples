import { Observable } from 'rxjs';
import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { ChangeName, UserModel } from '../state-management/user.state';
import { AddItem, ItemModel } from '../state-management/item.state';


@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
})
export class MyAppComponent {
    public itemId: string;
    public itemName: string;
    public name: string;
    public state$: Observable<any>; // TODO: Built wront. "user" should be accesable
    public items: ItemModel[] = [];

    constructor(private store: Store) {
        this.state$ = store.select(state => state);
    }

    addItem() {
        console.log('this', this);
        this.state$.subscribe(what => {
            console.log('state$ subscription:', what);
            this.items = what.user.items;
            console.log('items = ', this.items);
            
        })
        
        this.store.dispatch(new AddItem({
            id: this.itemId,
            name: this.itemName
        })).subscribe((el) => console.log('AddItem dispatched', el))
    }

    changeName() {
        this.store.dispatch(new ChangeName(`${this.name}`));
    }
}