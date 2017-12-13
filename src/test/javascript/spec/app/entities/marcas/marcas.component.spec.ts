/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { MarcasComponent } from '../../../../../../main/webapp/app/entities/marcas/marcas.component';
import { MarcasService } from '../../../../../../main/webapp/app/entities/marcas/marcas.service';
import { Marcas } from '../../../../../../main/webapp/app/entities/marcas/marcas.model';

describe('Component Tests', () => {

    describe('Marcas Management Component', () => {
        let comp: MarcasComponent;
        let fixture: ComponentFixture<MarcasComponent>;
        let service: MarcasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [MarcasComponent],
                providers: [
                    MarcasService
                ]
            })
            .overrideTemplate(MarcasComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarcasComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarcasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Marcas(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.marcas[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
