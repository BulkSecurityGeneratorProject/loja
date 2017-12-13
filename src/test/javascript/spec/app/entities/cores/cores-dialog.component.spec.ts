/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { CoresDialogComponent } from '../../../../../../main/webapp/app/entities/cores/cores-dialog.component';
import { CoresService } from '../../../../../../main/webapp/app/entities/cores/cores.service';
import { Cores } from '../../../../../../main/webapp/app/entities/cores/cores.model';

describe('Component Tests', () => {

    describe('Cores Management Dialog Component', () => {
        let comp: CoresDialogComponent;
        let fixture: ComponentFixture<CoresDialogComponent>;
        let service: CoresService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CoresDialogComponent],
                providers: [
                    CoresService
                ]
            })
            .overrideTemplate(CoresDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CoresDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoresService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Cores(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.cores = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'coresListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Cores();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.cores = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'coresListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
