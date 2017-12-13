/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { MarcasDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/marcas/marcas-delete-dialog.component';
import { MarcasService } from '../../../../../../main/webapp/app/entities/marcas/marcas.service';

describe('Component Tests', () => {

    describe('Marcas Management Delete Component', () => {
        let comp: MarcasDeleteDialogComponent;
        let fixture: ComponentFixture<MarcasDeleteDialogComponent>;
        let service: MarcasService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [MarcasDeleteDialogComponent],
                providers: [
                    MarcasService
                ]
            })
            .overrideTemplate(MarcasDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarcasDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarcasService);
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
