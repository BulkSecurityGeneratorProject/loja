/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { CadastrosLocalidadesDialogComponent } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades-dialog.component';
import { CadastrosLocalidadesService } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.service';
import { CadastrosLocalidades } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.model';
import { CadastrosService } from '../../../../../../main/webapp/app/entities/cadastros';
import { LocalidadesService } from '../../../../../../main/webapp/app/entities/localidades';

describe('Component Tests', () => {

    describe('CadastrosLocalidades Management Dialog Component', () => {
        let comp: CadastrosLocalidadesDialogComponent;
        let fixture: ComponentFixture<CadastrosLocalidadesDialogComponent>;
        let service: CadastrosLocalidadesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosLocalidadesDialogComponent],
                providers: [
                    CadastrosService,
                    LocalidadesService,
                    CadastrosLocalidadesService
                ]
            })
            .overrideTemplate(CadastrosLocalidadesDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosLocalidadesDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosLocalidadesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CadastrosLocalidades(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.cadastrosLocalidades = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cadastrosLocalidadesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CadastrosLocalidades();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.cadastrosLocalidades = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'cadastrosLocalidadesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
