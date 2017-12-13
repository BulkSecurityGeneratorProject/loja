/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { CadastrosLocalidadesDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades-delete-dialog.component';
import { CadastrosLocalidadesService } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.service';

describe('Component Tests', () => {

    describe('CadastrosLocalidades Management Delete Component', () => {
        let comp: CadastrosLocalidadesDeleteDialogComponent;
        let fixture: ComponentFixture<CadastrosLocalidadesDeleteDialogComponent>;
        let service: CadastrosLocalidadesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosLocalidadesDeleteDialogComponent],
                providers: [
                    CadastrosLocalidadesService
                ]
            })
            .overrideTemplate(CadastrosLocalidadesDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosLocalidadesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosLocalidadesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
