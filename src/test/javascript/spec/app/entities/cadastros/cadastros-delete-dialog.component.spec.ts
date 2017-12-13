/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { CadastrosDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/cadastros/cadastros-delete-dialog.component';
import { CadastrosService } from '../../../../../../main/webapp/app/entities/cadastros/cadastros.service';

describe('Component Tests', () => {

    describe('Cadastros Management Delete Component', () => {
        let comp: CadastrosDeleteDialogComponent;
        let fixture: ComponentFixture<CadastrosDeleteDialogComponent>;
        let service: CadastrosService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosDeleteDialogComponent],
                providers: [
                    CadastrosService
                ]
            })
            .overrideTemplate(CadastrosDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosService);
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
