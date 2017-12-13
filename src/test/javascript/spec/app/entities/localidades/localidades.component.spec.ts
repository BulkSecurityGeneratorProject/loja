/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { LocalidadesComponent } from '../../../../../../main/webapp/app/entities/localidades/localidades.component';
import { LocalidadesService } from '../../../../../../main/webapp/app/entities/localidades/localidades.service';
import { Localidades } from '../../../../../../main/webapp/app/entities/localidades/localidades.model';

describe('Component Tests', () => {

    describe('Localidades Management Component', () => {
        let comp: LocalidadesComponent;
        let fixture: ComponentFixture<LocalidadesComponent>;
        let service: LocalidadesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [LocalidadesComponent],
                providers: [
                    LocalidadesService
                ]
            })
            .overrideTemplate(LocalidadesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LocalidadesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LocalidadesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Localidades(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.localidades[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
