/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { PedidosComponent } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.component';
import { PedidosService } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.service';
import { Pedidos } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.model';

describe('Component Tests', () => {

    describe('Pedidos Management Component', () => {
        let comp: PedidosComponent;
        let fixture: ComponentFixture<PedidosComponent>;
        let service: PedidosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [PedidosComponent],
                providers: [
                    PedidosService
                ]
            })
            .overrideTemplate(PedidosComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PedidosComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PedidosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Pedidos(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pedidos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
