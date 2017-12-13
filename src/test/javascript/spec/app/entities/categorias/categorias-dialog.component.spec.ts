/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { CategoriasDialogComponent } from '../../../../../../main/webapp/app/entities/categorias/categorias-dialog.component';
import { CategoriasService } from '../../../../../../main/webapp/app/entities/categorias/categorias.service';
import { Categorias } from '../../../../../../main/webapp/app/entities/categorias/categorias.model';

describe('Component Tests', () => {

    describe('Categorias Management Dialog Component', () => {
        let comp: CategoriasDialogComponent;
        let fixture: ComponentFixture<CategoriasDialogComponent>;
        let service: CategoriasService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CategoriasDialogComponent],
                providers: [
                    CategoriasService
                ]
            })
            .overrideTemplate(CategoriasDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategoriasDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategoriasService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Categorias(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.categorias = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'categoriasListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Categorias();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.categorias = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'categoriasListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
