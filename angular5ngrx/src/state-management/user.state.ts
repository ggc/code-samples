import { State, Action, StateContext } from '@ngxs/store';
import { ItemModel, AddItem } from './item.state';

export class ChangeName {
    static readonly type = 'ChangeName';
    constructor(
        public newName: string
    ) { }
}

export interface UserModel {
    name: string;
    email: string;
    age: number;
    items: ItemModel[];
}

@State<UserModel>({
    name: 'user',
    defaults: {
        name: '',
        email: '',
        age: 18,
        items: []
    }
})
export class UserState {
    @Action(AddItem)
    addItem(ctx: StateContext<UserModel>, action: AddItem) {
        const state = ctx.getState();
        ctx.patchState({
            ...state,
            items: [
                ...state.items,
                action.addItem
            ]
        });
    }

    @Action(ChangeName)
    changeName(ctx: StateContext<UserModel>, action: ChangeName) {
        
        const state = ctx.getState();
        ctx.setState({
            ...state,
            name: action.newName
        });
    }
}