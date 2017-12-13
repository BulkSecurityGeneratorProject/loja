/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { TamanhosDialogComponent } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos-dialog.component';
import { TamanhosService } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.service';
import { Tamanhos } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.model';

describe('Component Tests', () => {

    describe('Tamanhos Management Dialog Component', () => {
        let comp: TamanhosDialogComponent;
        let fixture: ComponentFixture<TamanhosDialogComponent>;
        let service: TamanhosService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [TamanhosDialogComponent],
                providers: [
                    TamanhosService
                ]
            })
            .overrideTemplate(TamanhosDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TamanhosDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TamanhosService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Tamanhos(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.tamanhos = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tamanhosListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Tamanhos();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.tamanhos = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tamanhosListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
