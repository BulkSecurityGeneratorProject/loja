import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Cores } from './cores.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CoresService {

    private resourceUrl = SERVER_API_URL + 'api/cores';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cores';

    constructor(private http: Http) { }

    create(cores: Cores): Observable<Cores> {
        const copy = this.convert(cores);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(cores: Cores): Observable<Cores> {
        const copy = this.convert(cores);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Cores> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Cores.
     */
    private convertItemFromServer(json: any): Cores {
        const entity: Cores = Object.assign(new Cores(), json);
        return entity;
    }

    /**
     * Convert a Cores to a JSON which can be sent to the server.
     */
    private convert(cores: Cores): Cores {
        const copy: Cores = Object.assign({}, cores);
        return copy;
    }
}
