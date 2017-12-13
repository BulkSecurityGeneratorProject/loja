/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { PedidosDetailComponent } from '../../../../../../main/webapp/app/entities/pedidos/pedidos-detail.component';
import { PedidosService } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.service';
import { Pedidos } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.model';

describe('Component Tests', () => {

    describe('Pedidos Management Detail Component', () => {
        let comp: PedidosDetailComponent;
        let fixture: ComponentFixture<PedidosDetailComponent>;
        let service: PedidosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [PedidosDetailComponent],
                providers: [
                    PedidosService
                ]
            })
            .overrideTemplate(PedidosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PedidosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PedidosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Pedidos(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.pedidos).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
