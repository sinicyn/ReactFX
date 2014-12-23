package org.reactfx.collection;

import java.util.Collections;
import java.util.List;

import org.reactfx.ObservableBase;
import org.reactfx.collection.ObsList.ChangeObserver;
import org.reactfx.util.AccuMap;
import org.reactfx.util.Lists;

abstract class ObsListBase<E>
extends ObservableBase<ChangeObserver<? super E>, ListChange<? extends E>>
implements ObsList<E>, AccessorListMethods<E> {

    public ObsListBase() {
        super(AccuMap.emptyListChangeAccumulationMap());
    }

    @Override
    public void addChangeObserver(ChangeObserver<? super E> listener) {
        addObserver(listener);
    }

    @Override
    public void removeChangeObserver(ChangeObserver<? super E> listener) {
        removeObserver(listener);
    }

    @Override
    public int hashCode() {
        return Lists.hashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        return Lists.equals(this, o);
    }

    @Override
    public String toString() {
        return Lists.toString(this);
    }

    @Override
    protected final boolean runUnsafeAction(Runnable action) {
        action.run();
        return true;
    }

    protected final void fireChange(ListChange<? extends E> change) {
        notifyObservers(ChangeObserver::onChange, change);
    }

    protected final void fireModification(TransientListModification<? extends E> mod) {
        fireChange(mod.asListChange());
    }

    protected final TransientListModification<E> elemReplacement(int index, E replaced) {
        return new TransientListModificationImpl<E>(
                this, index, index+1, Collections.singletonList(replaced));
    }

    protected final void fireElemReplacement(int index, E replaced) {
        fireModification(elemReplacement(index, replaced));
    }

    protected final TransientListModification<E> contentReplacement(List<E> removed) {
        return new TransientListModificationImpl<E>(this, 0, size(), removed);
    }

    protected final void fireContentReplacement(List<E> removed) {
        fireModification(contentReplacement(removed));
    }

    protected final TransientListModification<E> elemInsertion(int index) {
        return rangeInsertion(index, 1);
    }

    protected final void fireElemInsertion(int index) {
        fireModification(elemInsertion(index));
    }

    protected final TransientListModification<E> rangeInsertion(int index, int size) {
        return new TransientListModificationImpl<E>(
                this, index, index + size, Collections.emptyList());
    }

    protected final void fireRangeInsertion(int index, int size) {
        fireModification(rangeInsertion(index, size));
    }

    protected final TransientListModification<E> elemRemoval(int index, E removed) {
        return new TransientListModificationImpl<E>(
                this, index, index, Collections.singletonList(removed));
    }

    protected final void fireElemRemoval(int index, E removed) {
        fireModification(elemRemoval(index, removed));
    }

    protected final TransientListModification<E> rangeRemoval(int index, List<E> removed) {
        return new TransientListModificationImpl<E>(
                this, index, index, removed);
    }

    protected final void fireRemoveRange(int index, List<E> removed) {
        fireModification(rangeRemoval(index, removed));
    }
}
