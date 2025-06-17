package by.belpost.qrmodule.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QRCodeMetadataCustomRepositoryImpl implements QRCodeMetadataCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QRCodeMetadata> search(String template,
                                       Integer parcelId,
                                       String path,
                                       LocalDate createdAt,
                                       String format,
                                       String contentType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<QRCodeMetadata> cq = cb.createQuery(QRCodeMetadata.class);
        Root<QRCodeMetadata> root = cq.from(QRCodeMetadata.class);

        List<Predicate> predicates = new ArrayList<>();

        if (template != null && !template.isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("template")), template.toLowerCase()));
        }

        if (parcelId != null) {
            predicates.add(cb.equal(root.get("parcelId"), parcelId));
        }

        if (path != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("path"), path.toLowerCase()));
        }

        if (createdAt != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdAt.atTime(23, 59, 59)));
        }

        if (format != null && !format.isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("format")), format.toLowerCase()));
        }

        if (contentType != null && !contentType.isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("contentType")), contentType.toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}

