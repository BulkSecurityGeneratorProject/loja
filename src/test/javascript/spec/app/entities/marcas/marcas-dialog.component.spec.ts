/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { MarcasDialogComponent } from '../../../../../../main/webapp/app/entities/marcas/marcas-dialog.component';
import { MarcasService } from '../../../../../../main/webapp/app/entities/marcas/marcas.service';
import { Marcas } from '../../../../../../main/webapp/app/entities/marcas/marcas.model';

describe('Component Tests', () => {

    describe('Marcas Management Dialog Component', () => {
        let comp: MarcasDialogComponent;
        let fixture: ComponentFixture<MarcasDialogComponent>;
        let service: MarcasService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [MarcasDialogComponent],
                providers: [
                    MarcasService
                ]
            })
            .overrideTemplate(MarcasDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarcasDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarcasService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Marcas(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.marcas = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'marcasListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Marcas();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.marcas = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'marcasListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
