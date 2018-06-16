import { State, Action, StateContext } from '@ngxs/store';
import { Add, Sub, Reset } from './counter.actions';

@State<number>({
    name: 'counter',
    defaults: 0
})
export class CounterState {
    @Action(Add)
    add(ctx: StateContext<number>) {
        const state = ctx.getState();
        ctx.setState(state + 1);
    }

    @Action(Sub)
    sub(ctx: StateContext<number>) {
        const state = ctx.getState();
        ctx.setState(state - 1);
    }

    @Action(Reset)
    reset(ctx: StateContext<number>) {
        const state = ctx.getState();
        ctx.setState(0);
    }
}