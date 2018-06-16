import { State, Action, StateContext } from '@ngxs/store';
import { UserModel } from './user.state';
import { store } from '@angular/core/src/render3/instructions';

export interface ItemModel {
    name: string;
    id: string;
}
export class AddItem {
    static readonly type = 'AddItem';
    constructor(
        public addItem: ItemModel
    ) { }
}

@State<ItemModel>({
    name: 'item',
    defaults: {
        name: '',
        id: ''
    }
})
export class ItemState {
}