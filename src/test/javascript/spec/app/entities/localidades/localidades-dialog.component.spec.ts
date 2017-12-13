/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { LocalidadesDialogComponent } from '../../../../../../main/webapp/app/entities/localidades/localidades-dialog.component';
import { LocalidadesService } from '../../../../../../main/webapp/app/entities/localidades/localidades.service';
import { Localidades } from '../../../../../../main/webapp/app/entities/localidades/localidades.model';

describe('Component Tests', () => {

    describe('Localidades Management Dialog Component', () => {
        let comp: LocalidadesDialogComponent;
        let fixture: ComponentFixture<LocalidadesDialogComponent>;
        let service: LocalidadesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [LocalidadesDialogComponent],
                providers: [
                    LocalidadesService
                ]
            })
            .overrideTemplate(LocalidadesDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LocalidadesDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LocalidadesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Localidades(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.localidades = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'localidadesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Localidades();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.localidades = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'localidadesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
